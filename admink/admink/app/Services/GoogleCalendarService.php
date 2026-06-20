<?php

namespace App\Services;

use Google\Client;
use Google\Service\Calendar;
use Google\Service\Calendar\Event;
use Exception;

class GoogleCalendarService
{
    protected $client;
    protected $service;
    protected $calendarId;

    public function __construct()
    {
        // 1. Inicializa o cliente do Google
        $this->client = new Client();
        
        // 2. Aponta para o arquivo .json configurado no .env
        $path = base_path(env('GOOGLE_APPLICATION_CREDENTIALS'));
        
        if (!file_exists($path)) {
            throw new Exception("Arquivo de credenciais do Google não foi encontrado.");
        }

        $this->client->setAuthConfig($path);
        
        // 3. Define que vamos usar o escopo do Calendar (Leitura e Escrita)
        $this->client->setScopes(Calendar::CALENDAR);
        
        // 4. Instancia o serviço da Agenda e pega o ID do .env
        $this->service = new Calendar($this->client);
        $this->calendarId = env('GOOGLE_CALENDAR_ID', 'primary');
    }

  
    public function criarAgendamento(array $dados)
    {
        try {
            // Monta o evento no formato que o Google exige
            $event = new Event([
                'summary'     => $dados['titulo'],
                'description' => $dados['descricao'] ?? '',
                'start'       => [
                    'dateTime' => $dados['data_inicio'], // Formato: Y-m-d\TH:i:s
                    'timeZone' => config('app.timezone', 'America/Sao_Paulo'),
                ],
                'end'         => [
                    'dateTime' => $dados['data_fim'], // Formato: Y-m-d\TH:i:s
                    'timeZone' => config('app.timezone', 'America/Sao_Paulo'),
                ],
            ]);

            // Envia para a API do Google
            $calendarEvent = $this->service->events->insert($this->calendarId, $event);

            // Retorna o ID do evento gerado pelo Google
            return $calendarEvent->getId();

        } catch (Exception $e) {
            // Se o Google estiver fora ou der erro de permissão, lança a exceção
            throw new Exception("Erro ao agendar no Google Calendar: " . $e->getMessage());
        }
    }
}