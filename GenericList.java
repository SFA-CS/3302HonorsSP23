/**
 * Author: JJB 
 * This class creates a generic linked list.
 */

public class GenericList<E> extends ListADT<E> {
    private Node<E> head;  // starting node for the list
    private int numElements;             // number of images of the list

    /**
     * Constructor to create an empty list.
     */
    public GenericList() {
        this.head = null;
        this.numElements = 0;
    }

    /**
     * Constructor to create a list with one element.
     * @param firstElement - item in the head node of the list
     */
    public GenericList(E firstElement) {
        this.head = new Node<E>(firstElement);
        this.numElements = 1;
    }

    /**
     * This method determines if a list is empty.
     * @return - true if the list is empty; false otherwise
     */
    @Override
    public boolean isEmpty() {
        return this.numElements == 0;
    }

    /* This method returns an integer 
     * for the size of the list.
     * @return - size of the list.
     * @see ListADT#size()
     */
    @Override
    public int size() {
        return this.numElements;
    }

    /**
     * This method emptys the list and
     * sets the fields to a default sate.
     */
    @Override
    public void removeAll() {
        // makes all nodes unreachable
        // and thus garbage collected
        this.head = null;
        this.numElements = 0;
    }

    /**
     * This private method is used to find the 
     * node at the given index in the list
     * @param index - integer between 0 and the numElements-1
     * @return the nodes by the position given by index
     */
    private Node<E> getNodeAt(int index) {
        Node<E> n = this.head;
        for (int k = 1; k <= index; k++)
            n = n.getNext();

        return n;
    }

    /**
     * This method adds an element at the specified position in the list.
     * @param index - integer representing the desired position to add the element
     * @param item - element to be added to the list
     * @return - non
     * @throws - ListException when index is negative or behyond numElements - 1
     */
    @Override
    public void add(int index, E item) throws ListException {
        if (index < 0 || index > this.size())
            throw new ListException("Index " + index + " is invalid for a list of size " + this.size());

        // create a node for the new element
        Node<E> newNode = new Node<E>(item);
        
        // adding to the beginning is handled separately 
        // because there is no previous node
        if (index == 0) {
            // head of the list becomes second 
            newNode.setNext(this.head);
            // new node becomes head of the list
            this.head = newNode;
        } else { // not the head
            Node<E> previous = this.getNodeAt(index - 1);
            Node<E> current = previous.getNext();

            // the node at index - 1 points to the new node
            // and the new node points to the node at index
            // which effectively moves current node to index+1
            previous.setNext(newNode);
            newNode.setNext(current);
        }
        this.numElements++; 
    }

    /**
     * This method returns the element stored at node given
     * by the index.
     * @param - index of the node containing the value the user
     * would like
     * @returns - value of the element held in the node at index 
     * @throws - an exception if index is negative or beyond the list size
     */
    @Override
    public E get(int index) throws ListException {
        if ((index < 0) || (index >= this.size()))
            throw new ListException("Index " + index + " is invalid for a list of size " + this.size());

        // get the node at the given index and return the value there
        Node<E> node = getNodeAt(index);
        return node.getItem();
    }

    /**
     * Removes a node at the given index (if it exists)
     * @param index - index of the node to remove
     * @throws ListException - when index is negative or
     * beyond size of the list.
     */
    @Override
    public void remove(int index) throws ListException {
        if ((index < 0) || (index >= this.size()))
            throw new ListException("Index " + index + " is invalid for a list of size " + this.size());

        if (index == 0)
            this.head = this.head.getNext();
        else {
            Node<E> previous = getNodeAt(index - 1);
            Node<E> nodeToRemove = previous.getNext();
            Node<E> nodeAfter = nodeToRemove.getNext();

            previous.setNext(nodeAfter);
        }
        this.numElements--;
    }

    /**
     * Displays contents of the list in string format
     * with [ ] delimiters.
     */
    @Override
    public String toString() {
        String s = "[";
        if (this.size() != 0) {
            Node<E> current = this.head;
            for (int i = 0; i < this.size() - 1; i++) {
                s = s + current.getItem().toString() + ",";
                current = current.getNext();
            }
            s = s + current.getItem().toString();
        }

        return s + "]";
    }

}
