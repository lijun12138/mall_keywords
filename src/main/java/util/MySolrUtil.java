package util;

import org.apache.solr.client.solrj.impl.HttpSolrServer;

public class MySolrUtil {
	private static HttpSolrServer solr = null;

	public static HttpSolrServer getSolr(String key) {
		solr = new HttpSolrServer(PropertiesUtil.getPath("solr.properties", key));
		return solr;
	}
}
