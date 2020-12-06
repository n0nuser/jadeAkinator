package comportamientos;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import utilidades.Pregunta;
import utilidades.Utils;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

@SuppressWarnings("serial")
public class ComportamientoQuienEsQuien extends CyclicBehaviour {

	@Override
	public void action() {
		/*
		 * --------------------------------------------------------------------------------
		 * ---------------------------------Variables--------------------------------------
		 * --------------------------------------------------------------------------------
		 */
		String respuesta = "";
		String content = "";
		String mensajeInicial = "\n================= QuienEsQuien =================\n\n";
		String textoPregunta = "PREGUNTA: ";
		ACLMessage msg = null;
		Pregunta pregunta = null;
		ConverterUtils.DataSource source = null;
		Instances dataEntrenamiento = null;
		
		/*
		 * --------------------------------------------------------------------------------
		 * -------------------------------Funciones Preguntas------------------------------
		 * --------------------------------------------------------------------------------
		 */

		try {
			source = new ConverterUtils.DataSource("famosos.csv");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			System.out.print("Error en DataSource: ");
			e1.printStackTrace();
			System.out.println();
		}

		try {
			dataEntrenamiento = source.getDataSet();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			System.out.print("Error en getDataSet: ");
			e1.printStackTrace();
			System.out.println();
		}

		// Indicar el atributo con la categoría a clasificar
		if (dataEntrenamiento.classIndex() == -1) {
			dataEntrenamiento.setClassIndex(0);
		}
		J48 j48 = new J48();
		try {
			j48.setOptions(new String[] { "-C", "0.25", "-M", "1" });
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			System.out.print("Error en setOptions: ");
			e1.printStackTrace();
			System.out.println();
		}

		j48.setUnpruned(true);
		try {
			j48.buildClassifier(dataEntrenamiento);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			System.out.print("Error en buildClassifier: ");
			e1.printStackTrace();
			System.out.println();
		}

		try {
			j48.classifyInstance(dataEntrenamiento.lastInstance());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			System.out.print("Error en classifyInstance: ");
			e1.printStackTrace();
			System.out.println();
		}

		try {
			pregunta = new Pregunta(j48);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			System.out.print("Error en new Pregunta(j48): ");
			e1.printStackTrace();
			System.out.println();
		}
		
		/*
		 * --------------------------------------------------------------------------------
		 * -------------------------------Inicio de Conexión-------------------------------
		 * --------------------------------------------------------------------------------
		 */

		// Descripción: Este inicio de conexión entre el agente QuienEsQuien y el Cliente solo
		// se ejecuta una única vez.
		
		// Aquí recibe el Hola Akinator
		msg = this.myAgent.blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.REQUEST));

		content = Utils.readMsg(msg);
		System.out.println("Respuesta de " + msg.getSender().getLocalName() + " con conversationID "
				+ msg.getConversationId() + " : " + content);
		
		
		/*
		 * --------------------------------------------------------------------------------
		 * ---------------------------------Preguntas--------------------------------------
		 * --------------------------------------------------------------------------------
		 */
		
		// Descripción: En esta parte solo se realiza el envio de preguntas al cliente. Primero
		// enviamos una pregunta inicial y después enviamos de forma continua las preguntas hasta 
		// adivinar el personaje famoso
		
		content = mensajeInicial;
		content += textoPregunta;
		content += pregunta.obtenerPreguntaNodo();
		Utils.sendMsg(this.myAgent, content, msg.getSender());
		msg = this.myAgent.blockingReceive(MessageTemplate.MatchConversationId(msg.getConversationId()));
		respuesta = Utils.readMsg(msg);
		pregunta.navegarNodoRespuesta(respuesta);

		// Se ejecuta de forma continúa
		while (!pregunta.esNodoFinal()) {
			content = textoPregunta;
			content += pregunta.obtenerPreguntaNodo();
			Utils.sendMsg(this.myAgent, content, msg.getSender());
			msg = this.myAgent.blockingReceive(MessageTemplate.MatchConversationId(msg.getConversationId()));
			respuesta = Utils.readMsg(msg);
			pregunta.navegarNodoRespuesta(respuesta);
		}
		// Manda respuesta final
		Utils.sendMsg(this.myAgent, "Es " + pregunta.obtenerPreguntaNodo(), msg.getSender());
	}
}
