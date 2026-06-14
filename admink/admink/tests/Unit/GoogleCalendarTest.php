<?php

namespace Tests\Unit;

use Tests\TestCase;
use App\Services\GoogleCalendarService;
use Illuminate\Foundation\Testing\RefreshDatabase;

class GoogleCalendarTest extends TestCase
{
   
    public function test_google_calendar_service_sync_simulates_event_successfully()
    {

        $agendamentoMock = json_decode(json_encode([
            'id_agendamento' => 3,
            'data_horario_inicio' => '2026-06-07 14:11:00',
            'data_horario_fim' => '2026-06-07 16:11:00'
        ]));


        $orcamentoMock = json_decode(json_encode([
            'id_orcamento' => 4
        ]));


        $service = new GoogleCalendarService();
        

        $result = $service->sync($agendamentoMock, $orcamentoMock);

        $this->assertEquals('mocked_event_id_123', $result);
    }
}