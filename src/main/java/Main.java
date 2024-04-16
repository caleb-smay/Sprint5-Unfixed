import java.util.ArrayList;




public class Main {

    //prints the route generated by path
    public static void printRoute(ArrayList<BestPath> currRoute){
        for (int i =0; i<currRoute.size()-1; i++){
            System.out.println("Curr Node: " + currRoute.get(i).NodeID() + " Next Node: " + currRoute.get(i).NextNodeID() + " Time: " + currRoute.get(i).TimeEstimate() );
        }
        System.out.println("Final Node: " + currRoute.get(currRoute.size()-1).NodeID() + " Total Time: " + currRoute.get(currRoute.size()-1).TimeEstimate());
    }

    public static void main(String[] args)  {
        //use dummy file
        String graphFile= "./build/resources/main/";
        String buildingID="Town Hall";
        DatabaseFetcher dbFetch= new DatabaseFetcher();
        BuildingResult currBuilding = dbFetch.findBuilding(buildingID);
        System.out.println(APIMethods.gsonify(currBuilding));
        graphFile +=currBuilding.GraphNodes();

        ArrayList<FloorResult> currFloors=dbFetch.findFloors(buildingID);
        System.out.println(APIMethods.gsonify(currFloors));
        //initialize graph
        AdjacencyList graph = new AdjacencyList();
        graph.createGraph(graphFile);
        graph.UpdateWeights();

        //create path object with sample start and end node
        PathGeneration path = new PathGeneration(graph.findNode(1), graph.findNode(7));

        //generate the sample path and print it
        ArrayList<BestPath>currRoute = path.createRoute(graph);
        printRoute(currRoute);

        //use a different destination to generate another sample path and print it
        path.setEnd(graph.findNode(21));
        currRoute=path.createRoute(graph);
        printRoute(currRoute);

        System.out.println(APIMethods.gsonify(currRoute));
    }
}
