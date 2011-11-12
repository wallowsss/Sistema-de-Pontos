package com.mack.prototipo;

public class Horario {

	private String data, hora_inicial, hora_final;
	
	public Horario(String data, String hora_inicial, String hora_final){
		this.data=data;
		this.hora_inicial=hora_inicial;
		this.hora_final =hora_final; 
	}
	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getHoraInicial() {
		return hora_inicial;
	}

	public void setHoraInicial(String hora_inicial) {
		this.hora_inicial = hora_inicial;
	}

	public String getHoraFinal() {
		return hora_final;
	}

	public void setHoraFinal(String hora_final) {
		this.hora_final = hora_final;
	} 
}
