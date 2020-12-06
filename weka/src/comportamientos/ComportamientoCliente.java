package comportamientos;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.Scanner;
import utilidades.Utils;

@SuppressWarnings("serial")
public class ComportamientoCliente extends CyclicBehaviour {

	@Override
	public void action() {
		/*
		 * --------------------------------------------------------------------------------
		 * ---------------------------------Variables--------------------------------------
		 * --------------------------------------------------------------------------------
		 */
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String texto = "", respuesta = "";
		
		
		/*
		 * --------------------------------------------------------------------------------
		 * -------------------------------Inicio de Conexión-------------------------------
		 * --------------------------------------------------------------------------------
		 */
		// Descripción: El cliente saluda a Akinator por primera vez. Ayuda a saber que la 
		// conexión se ha establecido.
		
		Utils.enviarMensaje(this.myAgent, "quienesquien", "Hola Akinator", ACLMessage.REQUEST);

		
		/*
		 * --------------------------------------------------------------------------------
		 * -----------------------------Preguntas y Respuestas-----------------------------
		 * --------------------------------------------------------------------------------
		 */
		
		while (!texto.startsWith("Es ")) {
			// Aquí recibe la pregunta
			ACLMessage msg = this.myAgent.blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
			texto = Utils.readMsg(msg);
			// Imprime la pregunta
			System.out.println(texto);
			// Si no es la respuesta final sigue
			if (!texto.startsWith("Es ")) {
				// Aquí­ responde
				while (true) {
					System.out.print("Introduzca respuesta (s/n): ");
					// Aquí responde s/n
					respuesta = scanner.nextLine().toLowerCase();
					if (respuesta.equals("s")) {
						break;
					} else if (respuesta.equals("n")) {
						break;
					}
				}
				// Aqui envia respuesta
				Utils.enviarMensaje(this.myAgent, "quienesquien", respuesta, ACLMessage.INFORM);
			} else {
				break;
			}
		}
	}
}
