<?php

use App\Agendamento;
use App\Services\GoogleCalendarService;

class GoogleCalendarServiceTest extends TestCase
{
    public function test_sync_returns_true_when_successful()
    {
        $agendamento = new Agendamento();
        $agendamento->id = 1;
        $agendamento->data_horario_inicio = '2026-06-10 10:00:00';
        $agendamento->data_horario_fim = '2026-06-10 11:00:00';

        $service = new GoogleCalendarService(function ($evento) {
            $this->assertEquals('Agendamento #1', $evento['summary']);
            $this->assertEquals('primary', $evento['calendarId']);
        });

        $resultado = $service->sync($agendamento);

        $this->assertTrue($resultado);
    }

    public function test_sync_returns_false_when_google_calendar_fails()
    {
        $agendamento = new Agendamento();
        $agendamento->id = 2;
        $agendamento->data_horario_inicio = '2026-06-10 14:00:00';
        $agendamento->data_horario_fim = '2026-06-10 15:00:00';

        $service = new GoogleCalendarService(function () {
            throw new Exception('Erro ao enviar evento para o Google Calendar');
        });

        $resultado = $service->sync($agendamento);

        $this->assertFalse($resultado);
    }
}
