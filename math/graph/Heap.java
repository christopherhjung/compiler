package graph;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Heap<E> {

	public static int DEFAULT_INIT_SIZE = 7;

	private Map<Object, Integer> map;

	private Object[] elements;
	private double[] priorities;

	private int size = 0;

	public Heap()
	{
		this( DEFAULT_INIT_SIZE );
	}

	public Heap( int initSize )
	{
		map = new HashMap<>();
		
		elements = new Object[initSize];
		priorities = new double[initSize];
	}

	/**
	 * Current count of elements in {@code Heap}
	 * @return current count of elements in {@code Heap}
	 */
	public int size()
	{
		return size;
	}

	/**
	 * Check if heap is empty
	 * @return true if {@code Heap} is empty 
	 */
	public boolean isEmpty()
	{
		return size == 0;
	}

	public void remove( E element )
	{
		int pos = map.get( element );
		size--;
		elements[pos] = elements[size];
		priorities[pos] = priorities[size];

		shiftDown( pos );
		shiftUp( pos );
	}

	public void changePriority( E element, double newPriority )
	{
		int pos = map.get( element );
		priorities[pos] = newPriority;

		shiftDown( pos );
		shiftUp( pos );
	}

	public E pop()
	{
		@SuppressWarnings("unchecked")
		E element = (E) elements[0];

		size--;
		elements[0] = elements[size];
		priorities[0] = priorities[size];
		map.remove( size );
		shiftDown( 0 );

		return element;
	}

	public void add( E element, double priority )
	{
		prepareSizeForAdd();

		elements[size] = element;
		priorities[size] = priority;
		map.put( element, size );
		shiftUp( size );
		size++;
	}

	private void prepareSizeForAdd()
	{
		if ( elements.length <= size )
		{
			Object[] newElements = new Object[2 * elements.length + 1];
			double[] newPriorities = new double[2 * priorities.length + 1];
			System.arraycopy( elements, 0, newElements, 0, elements.length );
			System.arraycopy( priorities, 0, newPriorities, 0, priorities.length );

			elements = newElements;
			priorities = newPriorities;
		}
	}

	private void shiftDown( int index )
	{
		while ( index < size )
		{
			int left = index * 2 + 1;
			int right = left + 1;
			if ( right >= size || priorities[left] < priorities[right] )
			{
				if ( left < size && priorities[index] > priorities[left] )
				{
					swap( index, left );
					index = left;
				}
				else break;
			}
			else if ( priorities[index] > priorities[right] )
			{
				swap( index, right );
				index = right;
			}
			else break;
		}
	}

	private void shiftUp( int index )
	{
		while ( index > 0 )
		{
			int nextIndex = index / 2;

			if ( priorities[index] < priorities[nextIndex] )
			{
				swap( index, nextIndex );
				index = nextIndex;
			}
			else break;
		}
	}

	private void swap( int a, int b )
	{
		Tools.swap( elements, a, b );
		Tools.swap( priorities, a, b );

		map.put( elements[a], a );
		map.put( elements[b], b );
	}

	@Override
	public String toString()
	{
		return Arrays.toString( elements );
	}

}
