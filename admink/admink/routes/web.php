<?php

Route::get('/', function () {
    return view('welcome');
});

Route::group(['middleware' => 'auth', 'middleware' => 'check.estudio'], function () {

    Route::prefix('admin')->name('admin.')->namespace('Admin')->group(function () {

        Route::get('/home', 'HomeController@index')->name('home');
        
        Route::get('/home/estudio/{id_estudio}', 'HomeController@estudio')->name('home.estudio');

        Route::resource('/clientes', 'ClienteController');
        
        Route::resource('/artistas', 'ArtistaController');
        
        Route::resource('/estacoes', 'EstacaoController')->parameters(['estacoes' => 'estacao']);
        
        Route::resource('/orcamentos', 'OrcamentoController');
        
        Route::get('/orcamentos/{orcamento}/cancelar/', 'OrcamentoController@cancelar')->name('orcamentos.cancelar');
        
        Route::get('/orcamentos/{orcamento}/recuperar/', 'OrcamentoController@recuperar')->name('orcamentos.recuperar');
        
        Route::resource('/agendamentos', 'AgendamentoController');

        Route::get('/agendamentos/{agendamento}/finalizar/', 'AgendamentoController@finalizar')->name('agendamentos.finalizar');
        
        Route::get('/agendamentos/{agendamento}/cancelar/', 'AgendamentoController@cancelar')->name('agendamentos.cancelar');

        Route::get('/downloadPDF/{id}','AgendamentoController@downloadPDF')->name('downloadPDF');
    });

});

Auth::routes(['register' => false, 'reset' => false]);

use App\Services\GoogleCalendarService;

Route::get('/teste-agenda', function () {
    try {
        $agenda = new GoogleCalendarService();
        
        $dadosExemplo = [
            'titulo' => 'Teste de Agendamento ADMink',
            'descricao' => 'Se você está lendo isso, a API do Google funcionou!',
            'data_inicio' => now()->addHours(1)->format('Y-m-d\TH:i:s'), // Daqui a 1 hora
            'data_fim' => now()->addHours(2)->format('Y-m-d\TH:i:s'),    // Daqui a 2 horas
        ];

        $idDoEvento = $agenda->criarAgendamento($dadosExemplo);

        return "Sucesso! Evento criado no Google com o ID: " . $idDoEvento;

    } catch (\Exception $e) {
        return "Ih, deu erro: " . $e->getMessage();
    }
});