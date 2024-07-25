import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EsCluster {

  List<EsClusterNode> nodes;

  public void execute() throws IOException, InterruptedException {
    for (EsClusterNode node : nodes) {
      node.start();
    }

    System.out.println("Done starting");

    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    System.out.println("Done sleeping");

    System.out.println("About to stop");
    for (EsClusterNode node: nodes) {
      node.stop();
    }

    System.out.println("Done stopping");

    System.out.println("LONG SLEEP");
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }


    System.out.println("About to start again");
    for (EsClusterNode node: nodes) {
      node.start();
    }
    System.out.println("Done starting");


    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public EsCluster(int numNodes) {

    nodes = new ArrayList<>();

      for (int i = 0; i < numNodes; i++) {
        String dataDirPath = "/tmp/node" + i;
        File dataDir = new File(dataDirPath);
        if (!dataDir.exists()) {
          boolean dirCreated = dataDir.mkdir();
          if (!dirCreated) {
            throw new RuntimeException("Could not create dir");
          }
        }

        EsClusterNode esClusterNode = new EsClusterNode(i, dataDirPath);
        nodes.add(esClusterNode);

      }
  }


}
