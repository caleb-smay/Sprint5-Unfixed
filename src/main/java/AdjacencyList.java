
import javax.lang.model.type.NullType;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;


public class AdjacencyList{

    private Map<Integer, Node> graph;

    public AdjacencyList(){
        graph=new HashMap<>();
    }
    public void addNode(Node newNode){
        this.graph.put(newNode.getID(), newNode);
    }

    public Node findNode(int targetID){
        return this.graph.get(targetID);
    }
    public Map<Integer,Node> getAdjacencyList(){return this.graph;}

    //check if a node for the edge exists and add the node and edge
    private void addEdge(Node node, FileEntry currEntry){

        if (this.findNode(/*EdgeID from file*/ currEntry.EdgeID())!=null){
            Edge edge = new Edge(this.findNode(currEntry.EdgeID()),currEntry.Weight());
            node.addEdge(edge);
        }
        else{
            Node edgePlaceholder = new Node(currEntry.EdgeID(),-99,Type.FINAL);
            this.addNode(edgePlaceholder);
            Edge edge = new Edge(edgePlaceholder,currEntry.Weight());
            node.addEdge(edge);
        }
    }

    //run after map fully added, update weights for all edges connected to transition nodes
    public void UpdateWeights(){

        for(Map.Entry<Integer, Node> entry : graph.entrySet()){
            if(entry.getValue().getType()==Type.TRANSITION)
            {
                TransitionWeight(entry.getValue());
            }
        }
    }


    //calculates appropriate edge weight for transition nodes, to aid in time estimation
    public void TransitionWeight(Node transition){
        //placeholder values
        int floorMin=99;
        int floorMax=-99;
        Iterator<Edge> edgeIterator = transition.getEdges().iterator();
        while(edgeIterator.hasNext()){
            int floor =edgeIterator.next().getSelf().getFloor();
            {
                if (floor<floorMin){
                    floorMin=floor;
                }
                if(floor>floorMax){
                    floorMax=floor;
                }

            }
        }
        int diff = (floorMax-floorMin);
        edgeIterator=transition.getEdges().iterator();
        while(edgeIterator.hasNext()){
            Edge currEdge= edgeIterator.next();
            int currWeight=currEdge.getWeight();
            currEdge.setWeight(currWeight*(diff*3));
        }
    }
    public void createGraph(String graphFile)  {
        FileReader graph = new FileReader(graphFile);


        //placeholders until file reading is set up
        FileEntry currEntry=new FileEntry(0,0,Type.FINAL, 0,0);
        //for each line, create or find Node, create Edge, add it to adjList
        if(this.findNode(/*ID from FILE*/currEntry.NodeID())==null){
            Node node = new Node(currEntry.NodeID(),currEntry.Floor(), currEntry.type());
            addEdge(node, currEntry);

        }

        else{
            if (this.findNode(currEntry.NodeID()).getFloor()==-99){
                Node node = this.findNode(currEntry.NodeID());
                node.setFloor(currEntry.Floor());
                node.setType(currEntry.type());
                //check if a node for the edge exists
                addEdge(node, currEntry);
            }
            else{
                Node node = this.findNode(currEntry.NodeID());
                //check if a node for the edge exists
                addEdge(node,currEntry);
            }
        }
    }

}



