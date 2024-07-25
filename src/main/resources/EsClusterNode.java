import org.testcontainers.containers.Container;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.utility.MountableFile;

import java.io.File;
import java.io.IOException;

public class EsClusterNode {

  private String dataDirPath;

  private int port;

  private ElasticsearchContainer elasticsearchContainer;

  private static final String ES_DOCKER_IMAGE_URL = "docker.elastic.co/elasticsearch/elasticsearch:8.11.3";
  private final String ELASTIC_USERNAME = "elastic";
  private final String ELASTIC_USER_PASSWORD = "ineedtoadminister";
  private final int ELASTICSEARCH_HTTP_PORT = 9200;

  private final String CONTAINER_DATA_DIR_PATH = "/usr/share/elasticsearch/data/";

  private final String HOST_DATA_DIR_PATH;
  private int nodeId;

  public EsClusterNode(int nodeId, String hostDataDirPath) {
    this.nodeId = nodeId;
    HOST_DATA_DIR_PATH = hostDataDirPath;

    elasticsearchContainer = new ElasticsearchContainer(ES_DOCKER_IMAGE_URL)
        .withPassword(ELASTIC_USER_PASSWORD)
        .withEnv("action.destructive_requires_name", "false")
        .withEnv("indices.id_field_data.enabled", "true");
/*
    elasticsearchContainer = elasticsearchContainer.withEnv("node.name", "localhost")
        .withEnv("network.bind_host", "0.0.0.0")
        .withEnv("network.publish_host", "localhost")
        .withEnv("transport.port", "9300")
        .withEnv("http.port", "9200")
        .withEnv("cluster.initial_master_nodes", "")
        .withEnv("discovery.seed_hosts", "");
*/
//    elasticsearchContainer = elasticsearchContainer.withFileSystemBind(HOST_DATA_DIR_PATH, CONTAINER_DATA_DIR_PATH);

    // Decrease the Elasticsearch Java heap size to avoid crashing the test container due to lack of memory
    elasticsearchContainer.addEnv("ES_JAVA_OPTS", "-Xms1g -Xmx1g");
  }

  public void start() throws IOException, InterruptedException {
    System.out.println ("start() was called");

    File previousSnapshot = new File("/tmp/data_dir_snapshot");
    if (previousSnapshot.exists()) {
      System.out.println("Snapshot file exists");

      File tmpDir = new File("/tmp/data_dir_snapshot_extraction");
      tmpDir.mkdir();
      try {
        Process p1 = Runtime.getRuntime().exec("tar xf /tmp/data_dir_snapshot --numeric-owner --same-owner --directory=/tmp/data_dir_snapshot_extraction/");
        p1.waitFor();
        System.out.println("Done with extraction");
//        p1 = Runtime.getRuntime().exec("chown -R 1000:1000 /tmp/data_dir_snapshot_extraction");
//        p1.waitFor();
        System.out.println("Done with chown");
      } catch (IOException e) {
        e.printStackTrace();
        System.exit(1);
      }

      System.out.println("Extracted tar");

      elasticsearchContainer = elasticsearchContainer.withCopyFileToContainer(MountableFile.forHostPath("/tmp/data_dir_snapshot_extraction/"), CONTAINER_DATA_DIR_PATH);

      elasticsearchContainer.withCommand("chown -R 1000:1000 /tmp/data_dir_snapshot_extraction/");
//      elasticsearchContainer.withCommand("chown", "-R", "1000:1000", "/tmp/data_dir_snapshot_extraction/", "&&", "sleep", "100");
//      elasticsearchContainer = elasticsearchContainer.withCommand("echo \"JULIAN\" && /usr/local/bin/docker-entrypoint.sh");
//      elasticsearchContainer.withCreateContainerCmdModifier((createContainerCmd) -> {
//        List<String> cmds = new ArrayList<>();
//        cmds.add("echo");
//        cmds.add("\"JULIAN\"");
//        cmds.add("&&");
//        cmds.addAll(Stream.of(createContainerCmd.getCmd()).collect(Collectors.toList()));
//
//        createContainerCmd.withCmd(cmds);
//      } );
    } else {
      System.out.println("Snapshot file does NOT exist");
    }
    elasticsearchContainer.start();

    Container.ExecResult execResult = elasticsearchContainer.execInContainer("ls", "-al", "/tmp");
    System.out.println("ERROR: " + execResult.getStderr());
    System.out.println("OUT: " + execResult.getStdout());
    System.out.println ("start() finished");
  }

  public void stop() throws IOException, InterruptedException {
    File file = new File("/tmp/data_dir_snapshot");
    if (file.exists()) {
      boolean deleted = file.delete();
      System.out.println("Deleted: " + deleted);
    }
    // ARCHIVE THE OLD DATA
    Container.ExecResult execResult = elasticsearchContainer.execInContainer("tar", "czf", "/tmp/data_dir_snapshot", "--numeric-owner", "--same-owner", "-C", CONTAINER_DATA_DIR_PATH, "--directory=" + CONTAINER_DATA_DIR_PATH, ".");
    System.out.println("ERROR: " + execResult.getStderr());
    System.out.println("OUT: " + execResult.getStdout());

    elasticsearchContainer.copyFileFromContainer("/tmp/data_dir_snapshot", "/tmp/data_dir_snapshot");
    elasticsearchContainer.stop();
  }

//  public void restart() throws IOException, InterruptedException {
//    elasticsearchContainer.getDockerClient().
//        String tag = elasticsearchContainer.getContainerId();
//    String snapshotId = elasticsearchContainer.getDockerClient().commitCmd(tag)
//        .withRepository("tempimg")
//        .withTag(tag).exec();
//    this.stop();
//    elasticsearchContainer.setDockerImageName("tempimg:" + tag);
//    this.start();
//  }

//  private class EsDataStartable implements Startable {

//    @Override
//    public void start() {
//      System.out.println("ESDATASTARTABLE");
//      boolean created = new File("/tmp/julian").mkdir();
//      boolean created = new File(CONTAINER_DATA_DIR_PATH + "/asdf").mkdir();
//      if (created) {
//        System.out.println("Created");
//      } else {
//        System.out.println("NOT created");
//      }
//      if (new File(CONTAINER_DATA_DIR_PATH).exists()) {
//        System.out.println("dir exists");
//      } else {
//        System.out.println("dir does not exists");
//      }
//    }
//
//    @Override
//    public void stop() {
//
//    }
//  }
}
