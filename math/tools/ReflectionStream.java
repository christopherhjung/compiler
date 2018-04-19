package tools;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.stream.BaseStream;
import java.util.stream.IntStream;

public class ReflectionStream implements BaseStream<Integer, ReflectionStream>{

	@Override
	public Iterator<Integer> iterator()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Spliterator<Integer> spliterator()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isParallel()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ReflectionStream sequential()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReflectionStream parallel()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReflectionStream unordered()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReflectionStream onClose( Runnable closeHandler )
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close()
	{
		// TODO Auto-generated method stub
		
	}

}
