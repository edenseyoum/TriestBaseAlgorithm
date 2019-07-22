public interface DataStreamAlgo {
	/*
	 * Handle the incoming edge
	 */
	public void handleEdge(Edge edge);

	/*
	 * Estimate number of triangles in the graph
	 */
	public int getEstimate();
}
