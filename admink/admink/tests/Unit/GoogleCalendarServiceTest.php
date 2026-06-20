<?php

namespace Tests\Unit;

use Tests\TestCase;
use App\Agendamento;
use App\Services\GoogleCalendarService;
use Mockery;

class GoogleCalendarServiceTest extends TestCase
{
    protected function tearDown(): void
    {
        Mockery::close();
        parent::tearDown();
    }

    /** @test */
    public function testDeveRetornarSucessoAoSincronizarAgendamento()
    {
        // 1. Preparação (Arrange)
        // Mock do modelo Agendamento para não precisar do banco real nos testes unitários
        $agendamentoMock = Mockery::mock(Agendamento::class);
        $agendamentoMock->shouldReceive('getAttribute')->with('id')->andReturn(123);
        
        $service = new GoogleCalendarService();

        // 2. Execução (Act)
        $resultado = $service->sync($agendamentoMock);

        // 3. Afirmação (Assert)
        $this->assertTrue($resultado);
    }

    /** @test */
    public function testDeveLancarExcecaoQuandoAGoogleApiFalhar()
    {
        // 1. Preparação (Arrange)
        $agendamentoMock = Mockery::mock(Agendamento::class);
        $agendamentoMock->shouldReceive('getAttribute')->with('id')->andReturn(123);

        $service = new GoogleCalendarService();

        // Expectativas de Erro (Devem ser declaradas ANTES da execução)
        $this->expectException(\Exception::class);
        $this->expectExceptionMessage('Falha ao conectar com o Google Calendar');

        // 2. Execução (Act) - Forçando o comportamento de erro simulado
        $service->syncWithError($agendamentoMock);
    }
}