<?php

namespace App\Services;

class GoogleCalendarService
{
    protected $client;

    public function __construct()
    {
        
        if (class_exists('\Google\Client')) {
            $this->client = new \Google\Client();
            $this->client->setClientId("123456-abcde" . ".apps.googleusercontent.com");
            $this->client->setClientSecret("GOCSPX-" . "clavesecreta");
            $this->client->setRedirectUri('http://127.0.0.1:8000/google/callback');
            $this->client->addScope(\Google\Service\Calendar::CALENDAR);
            $this->client->setAccessType('offline');
            $this->client->setPrompt('select_account consent');
        } else {
            $this->client = null;
        }
    }


    public function sync($agendamento, $orcamento)
    {

        $googleToken = session('google_token') ?? [
            'access_token' => env('GOOGLE_ACCESS_TOKEN_TESTE', 'fake-token'),
            'refresh_token' => env('GOOGLE_REFRESH_TOKEN_TESTE', 'fake-refresh')
        ];


        $eventData = [
            'summary'     => 'Agendamento - Estúdio (ID: ' . $agendamento->id_agendamento . ')',
            'description' => 'Agendamento associado ao Orçamento #' . $orcamento->id_orcamento,
            'start_time'  => \Carbon\Carbon::parse($agendamento->data_horario_inicio)->format(\DateTime::ATOM),
            'end_time'    => \Carbon\Carbon::parse($agendamento->data_horario_fim)->format(\DateTime::ATOM),
        ];


        if (!$this->client || $googleToken['access_token'] === 'fake-token') {
            \Log::info('GoogleCalendarService [Sprout Class Sync]: Evento simulado con éxito.', $eventData);
            return 'mocked_event_id_123';
        }


        $this->client->setAccessToken($googleToken);
        if ($this->client->isAccessTokenExpired() && $this->client->getRefreshToken()) {
            $this->client->fetchAccessTokenWithRefreshToken($this->client->getRefreshToken());   
        }

        $calendarService = new \Google\Service\Calendar($this->client);
        $event = new \Google\Service\Calendar\Event([
            'summary'     => $eventData['summary'],
            'description' => $eventData['description'],
            'start'       => ['dateTime' => $eventData['start_time'], 'timeZone' => 'America/Sao_Paulo'],
            'end'         => ['dateTime' => $eventData['end_time'], 'timeZone' => 'America/Sao_Paulo'],
        ]);

        return $calendarService->events->insert('primary', $event);
    }
}