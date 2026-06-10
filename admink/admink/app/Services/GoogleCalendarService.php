<?php

namespace App\Services;

use App\Agendamento;
use Exception;

class GoogleCalendarService
{
    protected $calendarClient;

    public function __construct(callable $calendarClient = null)
    {
        $this->calendarClient = $calendarClient;
    }

    public function sync(Agendamento $agendamento)
    {
        try {
            $evento = $this->buildEvent($agendamento);

            return $this->createEvent($evento);
        } catch (Exception $e) {
            return false;
        }
    }

    public function buildEvent(Agendamento $agendamento)
    {
        return [
            'summary' => 'Agendamento #' . $agendamento->id,
            'start' => [
                'dateTime' => $this->formatDate($agendamento->data_horario_inicio),
                'timeZone' => env('GOOGLE_CALENDAR_TIMEZONE', 'America/Sao_Paulo'),
            ],
            'end' => [
                'dateTime' => $this->formatDate($agendamento->data_horario_fim),
                'timeZone' => env('GOOGLE_CALENDAR_TIMEZONE', 'America/Sao_Paulo'),
            ],
            'calendarId' => env('GOOGLE_CALENDAR_ID', 'primary'),
        ];
    }

    protected function createEvent(array $evento)
    {
        if ($this->calendarClient) {
            call_user_func($this->calendarClient, $evento);
        }

        return true;
    }

    protected function formatDate($date)
    {
        if ($date instanceof \DateTime) {
            return $date->format('Y-m-d\TH:i:s');
        }

        return date('Y-m-d\TH:i:s', strtotime($date));
    }
}
