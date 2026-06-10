<?php

namespace App\Services;

use App\Agendamento;
use Exception;

class GoogleCalendarService
{
    public function sync(Agendamento $agendamento)
    {
        try {

            $evento = [
                'titulo' => 'Agendamento #' . $agendamento->id,
                'inicio' => $agendamento->data_horario_inicio,
                'fim'    => $agendamento->data_horario_fim,
            ];

            return true;

        } catch (Exception $e) {

            return false;

        }
    }
}
