package agentes;

import comportamientos.ComportamientoCliente;
import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

@SuppressWarnings("serial")
public class Cliente extends Agent {

	public void setup() {
		
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());

		ServiceDescription sd = new ServiceDescription();
		sd.setName("Cliente");
		sd.setType("cliente");
		sd.addOntologies("ontologia");
		sd.addLanguages(new SLCodec().getName());

		dfd.addServices(sd);

		try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			System.err.println("Error : Cliente.setup : Agent " + getLocalName() + ": " + e.getMessage());
		}
		
		addBehaviour(new ComportamientoCliente());
	}
	
}
