package agentes;

import comportamientos.ComportamientoQuienEsQuien;
import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.core.behaviours.ParallelBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.core.behaviours.ThreadedBehaviourFactory;

@SuppressWarnings("serial")
public class QuienEsQuien extends Agent {
	
    public void setup() {
    	
            DFAgentDescription dfd = new DFAgentDescription();
            dfd.setName(getAID());

            ServiceDescription sd = new ServiceDescription();
            sd.setName("QuienEsQuien");
            sd.setType("quienesquien");
            sd.addOntologies("ontologia");
            sd.addLanguages(new SLCodec().getName());

            dfd.addServices(sd);

            try {
                    DFService.register(this, dfd);
            } catch (FIPAException e) {
                    System.err.println("Error : QuienEsQuien.setup : Agent " + getLocalName() + ": " + e.getMessage());
            }
            
            ParallelBehaviour parallelBehaviour = new ParallelBehaviour();
            ThreadedBehaviourFactory threadedBehaviourFactory = new ThreadedBehaviourFactory();
            ComportamientoQuienEsQuien c1 = new ComportamientoQuienEsQuien();
            ComportamientoQuienEsQuien c2 = new ComportamientoQuienEsQuien();
            ComportamientoQuienEsQuien c3 = new ComportamientoQuienEsQuien();
            ComportamientoQuienEsQuien c4 = new ComportamientoQuienEsQuien();
            ComportamientoQuienEsQuien c5 = new ComportamientoQuienEsQuien();
    		parallelBehaviour.addSubBehaviour(threadedBehaviourFactory.wrap(c1));
    		parallelBehaviour.addSubBehaviour(threadedBehaviourFactory.wrap(c2));
    		parallelBehaviour.addSubBehaviour(threadedBehaviourFactory.wrap(c3));
    		parallelBehaviour.addSubBehaviour(threadedBehaviourFactory.wrap(c4));
    		parallelBehaviour.addSubBehaviour(threadedBehaviourFactory.wrap(c5));
    		addBehaviour(parallelBehaviour);
    		
    }
}
