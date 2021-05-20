package projeto.algorithms_process_mining.alpha_miner_prom;

import org.deckfour.xes.model.XLog;
import org.processmining.alphaminer.algorithms.AlphaClassicMinerImpl;
import org.processmining.alphaminer.parameters.AlphaMinerParameters;
import org.processmining.framework.util.Pair;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.graphbased.directed.petrinet.PetrinetEdge;
import org.processmining.models.graphbased.directed.petrinet.PetrinetNode;
import org.processmining.models.graphbased.directed.petrinet.elements.Place;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.models.semantics.petrinet.Marking;
import projeto.algorithms_process_mining.FootprintMatrix;
import projeto.api.dtos.NodeFrequencyDTO;
import projeto.api.dtos.conformance.deviations.NodeRelationDeviationsMap;
import projeto.core.Event;
import projeto.data.XESHelper;

import java.util.*;

public class FootprintAlphaProm extends FootprintMatrix {
    private AlphaMinerProm alphaMinerProm;

    private List<Integer> startNodes;
    private List<Integer> endNodes;
    private List<String> activities;
    protected List<NodeRelationDeviationsMap> relations;

    public FootprintAlphaProm(AlphaMinerProm algorithm, List<Event> events, LinkedHashSet<String> eventNames, boolean statistics) {
        super(algorithm, eventNames, statistics);
        this.alphaMinerProm = algorithm;
        getGraph(getEventLog(events));

        super.eventNames = new LinkedHashSet<>(activities);
        super.eventNamesMapper = buildEventsMapper(super.eventNames);
        for (Integer startNode : startNodes) {
            super.startEvents.put(startNode,1);
        }
        for (Integer endNode : endNodes) {
            super.endEvents.put(endNode,1);
        }
    }

    private XLog getEventLog(List<Event> events){
        String csvContent = XESHelper.eventsToCsv(events);//converter para csv
        return XESHelper.eventsCsvToXes(csvContent);
    }

    private void getGraph(XLog log){
        AlphaMinerParameters parameters = new AlphaMinerParameters();
        Pair<Petrinet, Marking> par = AlphaClassicMinerImpl.run(log,log.getClassifiers().get(0),parameters);
        //Transitions = nodes
        //arcs = edges
        Petrinet net = par.getFirst();
        activities = new LinkedList<>();
        Collection<Transition> transitions = net.getTransitions();
        for (Transition transition : transitions) {
            activities.add(transition.getLabel());
        }

        startNodes = new LinkedList<>();
        endNodes = new LinkedList<>();
        relations = new LinkedList<>();

        Set<PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>> edges = net.getEdges();
        for (PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> edge : edges) {
            PetrinetNode sourceNode = edge.getSource();
            PetrinetNode targetNode = edge.getTarget();
            //Preencher start nodes
            if (sourceNode.getLabel().equals("Start")){
                //Quer dizer que é uma activity simples
                if (targetNode.getClass() == Transition.class){
                    String label = targetNode.getLabel();
                    if (activities.contains(label)){
                        startNodes.add(activities.indexOf(label));
                    }
                }else {

                }
            }
            //Preencher end nodes
            if (targetNode.getLabel().equals("End")){
                if (sourceNode.getClass() == Transition.class){
                    String label = sourceNode.getLabel();
                    if (activities.contains(label)){
                        endNodes.add(activities.indexOf(label));
                    }
                }else {

                }
            }
            //Restantes
            if (!targetNode.getLabel().equals("End") && !sourceNode.getLabel().equals("Start")){
                //Tratar apenas os pontos?
                if (targetNode.getClass() == Place.class){//Quer dizer que o source é um Transition??
                    String sourceLabel = sourceNode.getLabel();
                    NodeRelationDeviationsMap relation = new NodeRelationDeviationsMap();
                    relation.setFrom(activities.indexOf(sourceLabel));//Se não for transition, isto rebenta aqui
                    List<NodeFrequencyDTO> toList = new LinkedList<>();
                    for (NodeRelationDeviationsMap r : relations) {
                        if (r.getFrom() == activities.indexOf(sourceLabel)){ //Se ja existir
                            relations.remove(r);
                            relation = r;
                            toList = r.getTo();
                            break;
                        }
                    }

                    for (String activity : activities) {
                        String[] targetLabel = targetNode.getLabel().split("],");
                        //Apenas quero a segunda parte da string. Esta string esta com fomato de array "[a,b],[c,d]"
                        if(targetLabel[1].contains(activity)){//Se a node conter a substring, exite relação- perigoso pode falhar(Pessimo)
                            //Existe relação
                            NodeFrequencyDTO nodeFrequencyDTO = new NodeFrequencyDTO();
                            nodeFrequencyDTO.setFrequency(1);
                            nodeFrequencyDTO.setNode(activities.indexOf(activity));
                            toList.add(nodeFrequencyDTO);
                        }
                    }
                    if (toList.size()>0){
                        relation.setTo(toList);
                        relations.add(relation);
                    }
                }
            }
        }

    }

    @Override
    public boolean areEventsConnected(int a, int b) {
        //Má performance 0n2
        for (NodeRelationDeviationsMap relation : relations) {
            if (relation.getFrom() == a){
                for (NodeFrequencyDTO nodeFrequencyDTO : relation.getTo()) {
                    if (nodeFrequencyDTO.getNode() == b)
                        return true;
                }
            }
        }
        return false;
    }
}
