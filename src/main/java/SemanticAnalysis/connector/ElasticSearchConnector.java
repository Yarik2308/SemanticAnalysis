package SemanticAnalysis.connector;

import WebCrawler.FilmsWeb;
import WebCrawler.Mappers;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

public class ElasticSearchConnector {

    private String clusterName = "SemanticAnalysis";
    //private String indexName = "semantic_analysis_index";
    private TransportClient client = null;

    public ElasticSearchConnector( String clusterName, String clusterIp, int clusterPort )
            throws UnknownHostException {

        Settings settings = Settings.builder()
                .put( "cluster.name", clusterName )
                .put( "client.transport.ignore_cluster_name", true )
                .put( "client.transport.sniff", true )
                .build();

        // create connection
        client = new PreBuiltTransportClient( settings );
        client.addTransportAddress( new TransportAddress( InetAddress.getByName( clusterIp ), clusterPort) );

        System.out.println( "Connection " + clusterName + "@" + clusterIp + ":"
                + clusterPort + " established!" );
    }

    public boolean isClusterHealthy() {
        final ClusterHealthResponse response = client
                .admin()
                .cluster()
                .prepareHealth()
                .setWaitForGreenStatus()
                .setTimeout(TimeValue.timeValueSeconds(2) )
                .execute()
                .actionGet();

        if (response.isTimedOut()) {
            System.out.println("The cluster is unhealthy: " + response.getStatus());
            return false;
        }

        System.out.println("The cluster is healthy: " + response.getStatus() );
        return true;
    }

    public boolean isIndexRegistered(String indexName) {
        // check if index already exists
        final IndicesExistsResponse ieResponse = client
                .admin()
                .indices()
                .prepareExists(indexName)
                .get(TimeValue.timeValueSeconds(1));

        // index not there
        if ( !ieResponse.isExists() ) {
            return false;
        }

        System.out.println( "Index already created!" );
        return true;
    }

    public boolean isIndexEmpty(String indexName){
        final IndicesStatsResponse indicesStatsResponse = client.admin().indices()
                .prepareStats(indexName).get();

        long size = indicesStatsResponse.getIndices().get(indexName).getTotal().docs.getCount();
        if(size>0){
            return false;
        }
        return true;
    }

    public boolean createIndex( String indexName, Integer numberOfShards, Integer numberOfReplicas ) {
        CreateIndexResponse createIndexResponse =
                client.admin().indices().prepareCreate( indexName )
                        .setSettings( Settings.builder()
                                .put("index.number_of_shards", numberOfShards )
                                .put("index.number_of_replicas", numberOfReplicas )
                        )
                        .get();

        if( createIndexResponse.isAcknowledged() ) {
            System.out.println("Created Index with " + numberOfShards + " Shard(s) and "
                    + numberOfReplicas + " Replica(s)!");
            return true;
        }

        return false;
    }

    public boolean bulkInsert(String indexName, String indexType) {
        BulkRequestBuilder bulkRequest = client.prepareBulk();

        Mappers mappers = new Mappers();
        List<FilmsWeb> filmsWeb = mappers.getAllFilmsWithooutComments();

        for (FilmsWeb filmWeb : filmsWeb) {
            // either use client#prepare, or use Requests# to directly build index/delete requests
            try {
                bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE).add(
                        client.prepareIndex(indexName, indexType, null)
                                .setSource(XContentFactory.jsonBuilder()
                                        .startObject()
                                        .field("id", filmWeb.getId())
                                        .field("name", filmWeb.getName())
                                        .field("description", filmWeb.getDescription())
                                        .field("genres", filmWeb.getGenres())
                                        .endObject()
                                ));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        BulkResponse bulkResponse = bulkRequest.get();
        if ( bulkResponse.hasFailures() ) {
            // process failures by iterating through each bulk response item
            System.out.println("Bulk insert failed!");
            return false;
        }

        return true;
    }

    public void close(){
        client.close();
    }
}
