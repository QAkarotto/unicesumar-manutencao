<?php

use App\Services\GoogleCalendarService;

class GoogleCalendarServiceTest extends TestCase
{
    public function test_sync_returns_true_when_successful()
    {
        $service = new GoogleCalendarService();

        $agendamento = new stdClass();
        $agendamento->id = 1;
        $agendamento->data_horario_inicio = '2026-06-10 10:00:00';
        $agendamento->data_horario_fim = '2026-06-10 11:00:00';

        $resultado = $service->sync($agendamento);

        $this->assertTrue($resultado);
    }

    public function test_sync_handles_error()
    {
        $service = new GoogleCalendarService();

        $resultado = false;

        $this->assertFalse($resultado);
    }
}
