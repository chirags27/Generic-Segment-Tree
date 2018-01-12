

class Node <T> {
	T val;
	int lValue;
	int rValue;
	Node <T> left;
	Node <T> right;
	
	public Node (T val, int lValue, int rValue, Node <T> left, Node <T> right) {
		this.val = val;
		this.lValue = lValue;
		this.rValue = rValue;
		this.left = left;
		this.right = right;
	}
}

interface Operation <T> {
	public T operate(T arg1, T arg2);
	
	// I + a = a
	// this will be useful for the base case
	public T getIdentityVal();
}

class SegmentTreeManager <T> {
	private Node <T> root = null;
	Operation <T> o = null; 
	
	public SegmentTreeManager(T array[], Operation <T> o) {
		// make a segment tree from array
		root = arrayToSegmentTree(array, 0, array.length - 1, o);
		this.o = o;
	}
	
	public T rangeQuery(int l, int r) {
		return rangeQueryI(l, r, root);
	}
	
	private T rangeQueryI(int l, int r, Node <T> current) {
		if (current == null) {
			return o.getIdentityVal();
		} else if (current.lValue == l && current.rValue == r) {
			return current.val;
		} else {
			int middle = (current.lValue + current.rValue)/2;
			if (l > middle) {
				// right
				return rangeQueryI(l, r, current.right);
			} else if (r <= middle) {
				// left
				return rangeQueryI(l, r, current.left);
			} else {
				// recurse on both sides
				// change the ranges though
				T left = rangeQueryI(l, middle, current.left);
				T right = rangeQueryI(middle + 1, r, current.right);
				return o.operate(left, right);
			}
		}
	}
	
	
	private Node <T> arrayToSegmentTree(T array[], int start, int end, Operation <T> o) {
		if (start > end) {
			return null;
		} else if (start == end) {
			return new Node <T> (array[start], start, end, null, null);
		} else {
			int middle = (start + end)/2;
			Node <T> left = arrayToSegmentTree(array, start, middle, o);
			Node <T> right = arrayToSegmentTree(array, middle + 1, end, o);
			T val = null;
			if (left != null) {
				val = o.operate(o.getIdentityVal(), left.val);
			}
			if (right != null) {
				val = o.operate(val, right.val);
			}
			Node <T> current = new Node <T> (val, start, end, left, right);
			return current;
		}
	}
}

public class Solution {
	
	
	
	public static void main(String args[]) {
		SegmentTreeManager <Integer> stm = new SegmentTreeManager<Integer>(
				new Integer[] {0,1,2,3,4,5}, 
				new Operation <Integer>() {
			@Override
			public Integer operate(Integer a, Integer b) {
				return a * b;
			}
			
			@Override
			public Integer getIdentityVal() {
				return 1;
			}
		});
		
		System.out.println(stm.rangeQuery(1, 5));
	}
}
