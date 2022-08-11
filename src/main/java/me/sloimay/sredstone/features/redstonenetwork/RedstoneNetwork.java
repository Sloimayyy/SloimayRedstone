package me.sloimay.sredstone.features.redstonenetwork;

import me.sloimay.sredstone.features.redstonenetwork.nodes.Node;
import me.sloimay.sredstone.utils.SFabricLib;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;

import static me.sloimay.sredstone.utils.SFabricLib.BlockUtils.BlockPosUtils.asLong;

/**
 * Represents a network of redstone components.
 */
public class RedstoneNetwork
{
    // ### Fields

    /**
     * The world in which resides the redstone network.
     */
    private World world;

    /**
     * The position of the root node.
     */
    private BlockPos startBlockPos;

    /**
     * The delays of each node from the root node.
     * Each node is characterized by its position toShortString().
     */
    private HashMap<Long, Set<Integer>> nodeTimings;

    /**
     * Holds some data about the node at the BlockPos long it's at.
     */
    private HashMap<Long, NodeMappingData> nodeMappingData;

    // # Mapping
    /**
     * The stack during the mapping that contains all the nodes
     * of the path until the node we're currently iterating from.
     */
    private Stack<Node> comingFrom;
    // #

    // ###



    // ### Init

    public RedstoneNetwork(World world, BlockPos startBlockPos)
    {
        this.world = world;
        this.startBlockPos = startBlockPos;

        this.nodeTimings = new HashMap<Long, Set<Integer>>();
    }

    // ###



    // ### Public methods

    /**
     * Maps the redstone network.
     *
     * @param maxSpan The maximum length a branch in the network can be.
     * @param maxTraversalCount How many times you can go over a node to add delays on it.
     */
    public void map(int maxSpan, int maxTraversalCount)
    {
        // Setup
        this.comingFrom = new Stack<Node>();

        // Start mapping
        this.map(Node.create(this.world, this.startBlockPos), maxSpan, maxTraversalCount, 0);
    }

    // ###



    // ### Private methods

    /**
     * Maps the redstone network from the inputted start node.
     *
     * @param startNode
     * @param maxSpan
     * @param maxTraversalCount
     * @param timing: The amount of game ticks from the root node.
     */
    private void map(Node startNode, int maxSpan, int maxTraversalCount, int timing)
    {
        /*

        Notes:
            // OUTDATED
            - First map the entire network with just nodes
            - then calculate the delay afterwards
            - and tHEn put all of these delays in a hashmap


            The stack is only here for traversing the network efficiently, so that
            when we stumble upon a redstone wire, we don't go back from where we
            came if it was also a redstone wire.
            However, a node's children is every node that gets its input from the
            node's outputs. It is not tied to the stack in any way.


            Logic: (arg: node)
                get how many times this node has been traversed
                if traversalCount > maxTraversalCount return;
                traversalCount++;

                register the timing of this node

                if node doesn't already have children:
                    populate children in our node

                nodeComingFrom = stack.peek()
                put node on the stack

                if we still have span stamina (span > 0)
                    for every children of our node:
                        if child is not at the same position as nodeComingFrom:
                            map(child, maxSpan - 1, maxOverlap)

                pop node out of the stack
                // OUTDATED


                Iterative BFS:

                stepCount = 0
                while ((queue is not empty) and stepCount < maxSpan)
                    allNodesInQueue = new ArrayList<Node>(queue)
                    empty the queue

                    Iterate through each node in allNodesInQueue:
                        node mapping data = get the node mapping data of this node

                        find the child nodes for each node
                        put all the child nodes in the queue




         */










        /* OLD RECURSIVE DFS IMPLEMENTATION

        // ## Get how many times this node has been traversed
        String nodeCharacterizationString = startNode.getPosition().toShortString();
        this.traversalCounts.putIfAbsent(nodeCharacterizationString, 0);
        int traversalCounts = this.traversalCounts.get(nodeCharacterizationString);
        // ## Abort if we're traversing it more than the max overlap
        if (traversalCounts > maxTraversalCount) return;
        // ## traversalCount++;
        this.traversalCounts.put(nodeCharacterizationString, this.traversalCounts.get(nodeCharacterizationString) + 1);



        // ## Register timing of this node
        this.registerNodeTiming(startNode, timing);

        // ## Populate the children of startNode
        if (startNode.getChildren().isEmpty())
            startNode.populateChildren();

        // ## Get the node we're coming from
        Node nodeComingFrom = this.comingFrom.empty() ? null : this.comingFrom.peek();
        // ## Put startNode on the stack
        this.comingFrom.push(startNode);

        // ## Map the children
        if (maxSpan > 0)
        {
            startNode.getChildren().forEach(children ->
            {
                // If the node coming from is null then we mapping every children
                if (nodeComingFrom == null)
                {
                    this.map(children, maxSpan - 1, maxTraversalCount, timing + children.getLatency());
                }
                else if(!SFabricLib.VectorUtils.blockPosEquals(nodeComingFrom.getPosition(), children.getPosition()))
                {
                    this.map(children, maxSpan - 1, maxTraversalCount, timing + children.getLatency());
                }
            });
        }


        // ## At the end, pop the stack
        if (!this.comingFrom.empty()) comingFrom.pop();

         */
    }

    /**
     * Registers a timing for the inputted node. There
     * can be multiple if the circuit loops.
     *
     * @param node
     * @param timing
     */
    private void registerNodeTiming(Node node, int timing)
    {
        nodeTimings.putIfAbsent(asLong(node.getPosition()), new HashSet<Integer>());
        nodeTimings.get(asLong(node.getPosition())).add(timing);
    }

    // ###



    // ### Getters

    public Set<Integer> getNodeTimings(BlockPos nodePosition)
    {
        String nodeCharacterizationString = nodePosition.toShortString();

        // If not a registered node, return null as it's not in the network.
        if (!nodeTimings.containsKey(nodeCharacterizationString))
            return null;

        // It is a registered node so we return the timings.
        return Collections.unmodifiableSet(nodeTimings.get(nodeCharacterizationString));
    }

    // ###



    // ### Classes

    /**
     * Wrapper for information relating nodes during mapping.
     */
    private static class NodeMappingData
    {
        private int traversalCount;
        private HashSet<Integer> timings;

        public NodeMappingData()
        {
            this.timings = new HashSet<Integer>();
        }

        public void setTraversalCount(int traversalCount) { this.traversalCount = traversalCount; }
        public int getTraversalCount() { return this.traversalCount; }

        public void addTiming(int timing) { this.timings.add(timing); }
        public Set<Integer> getTimings() { return Collections.unmodifiableSet(this.timings); }
    }

    // ###
}
