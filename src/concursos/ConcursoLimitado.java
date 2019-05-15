/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package concursos;

import java.util.ArrayList;


public class ConcursoLimitado extends Concurso {
    protected int numeroIntentos;
    private ArrayList<Intento> intentos;
    
    public ConcursoLimitado(String nombre, int numProblemas, int tiempoConcurso, int numIntento) {
        super(nombre, numProblemas, tiempoConcurso);
        this.numeroIntentos = numIntento;
        intentos = new ArrayList<>();
    }
    
    @Override
    protected Object clone() throws CloneNotSupportedException {
        ConcursoLimitado clon = new ConcursoLimitado(this.nombre, this.numProblemas, this.tiempoConcurso, this.numeroIntentos);
        clon.equipos = (ArrayList<String>) this.equipos.clone();
        clon.soluciones = (ArrayList<String>) this.soluciones.clone();
        for(int i = 0; i <  clon.equipos.size(); i++){
            for(int j = 1; j <= clon.soluciones.size(); j++){ 
                clon.intentos.add(new Intento(clon.equipos.get(i), j));
            }
        }
        return clon;
    }
    
    public int obtenerIntento(String equipo, int prob){
        for(int j = 0; j < this.intentos.size(); j++){ 
            if(intentos.get(j).getEquipo().equals(equipo) && intentos.get(j).getNumPregunta() == prob){
                return intentos.get(j).getNumPregunta();
            }
        }
        this.intentos.add(new Intento(equipo, prob));
        
        return 0;
    }
    
    public void cambiarIntento(String equipo, int prob, int intent){
        for(int j = 0; j < this.intentos.size(); j++){ 
            if(intentos.get(j).getEquipo().equals(equipo) && intentos.get(j).getNumPregunta() == prob){
                intentos.get(j).setNumIntento(intent);
            }
        }
    }

    @Override
    public Envio registrarEnvio(String equipo, int numeroProblema, String respuesta) {
        if(infoCorrecta(equipo, numeroProblema, respuesta) && this.enMarcha() && !respuestaAceptada(equipo, numeroProblema)){
            
            int intent = obtenerIntento(equipo, numeroProblema);
            if(intent < numeroIntentos){
                Estado estado;
                if(this.conseguirSolucion(numeroProblema).equals(respuesta)){
                    estado = Estado.ACEPTADO;
                    cambiarPuntos(equipo, 3);
                }else{
                    estado = Estado.RECHAZADO;
                    cambiarPuntos(equipo, -1);
                }
                Envio envio = new Envio(equipo, numeroProblema, respuesta, estado);
                envios.add(envio);
                intent++;
                cambiarIntento(equipo, numeroProblema, intent);
                return envio;
            }else{
                return null;
            }
        }else{
            return null;
        }
    }
}
