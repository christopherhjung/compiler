package therms;

import java.util.ArrayList;
import java.util.List;

import parser.ThermStringify;

public class Chain extends Therm {

	private final Therm outer;
	private final Therm inner;
	private final Variable outerReplace;

	public Chain( Therm outer, Therm inner )
	{
		this( outer, inner, Variable.X );
	}

	public Chain( Therm outer, Therm inner, Variable outerReplace )
	{
		this.outer = outer;
		this.inner = inner;
		this.outerReplace = outerReplace;
	}

	@Override
	public Therm replace( Therm replacer, Therm replacement )
	{
		Therm newOuter = outerReplace.equals( replacer ) ? outer : outer.replace( replacer, replacement );
		Therm newInner = inner.replace( replacer, replacement );

		if ( !newOuter.contains( outerReplace ) ) return newOuter;
		if ( newOuter == outer && newInner == inner ) return this;
		return new Chain( newOuter, newInner );
	}

	@Override
	public Therm derivate( Variable name )
	{
		return inner.derivate( name ).mul( outer.derivate( outerReplace ).replace( outerReplace, inner ) );
	}

	@Override
	public double valueAt( VarSet varSet )
	{
		return outer.valueAt( varSet.extend( outerReplace, inner.valueAt( varSet ) ) );
	}

	@Override
	public Therm simplify()
	{
		Therm simplifiedOuter = outer.simplify();
		Therm simplifiedInner = inner.simplify();

		if ( !simplifiedOuter.contains( outerReplace ) )
		{
			return simplifiedOuter;
		}
		else if ( simplifiedInner instanceof Const )
		{
			return new Const(
					simplifiedOuter.valueAt( new VarSet( outerReplace, ((Const) simplifiedInner).getValue() ) ) );
		}
		else if ( simplifiedOuter instanceof Multiply )
		{
			Multiply multiply = (Multiply) simplifiedOuter;
			List<Therm> consts = new ArrayList<>();
			List<Therm> variables = new ArrayList<>();

			for ( Therm therm : multiply )
			{
				if ( therm.contains( outerReplace ) )
				{
					variables.add( therm );
				}
				else
				{
					consts.add( therm );
				}
			}

			Therm newChain = new Chain( new Multiply( variables ), simplifiedInner, outerReplace );
			if ( consts.isEmpty() )
			{
				return newChain;
			}
			else
			{
				consts.add( newChain );
				return new Multiply( consts );
			}
		}
		else if ( simplifiedOuter instanceof Additional )
		{
			Additional additional = (Additional) simplifiedOuter;
			List<Therm> consts = new ArrayList<>();
			List<Therm> variables = new ArrayList<>();

			for ( Therm therm : additional )
			{
				if ( therm.contains( outerReplace ) )
				{
					variables.add( therm );
				}
				else
				{
					consts.add( therm );
				}
			}

			consts.add( new Chain( new Additional( variables ), simplifiedInner, outerReplace ) );
			return new Additional( consts );
		}
		else if ( simplifiedOuter instanceof Exponenional && simplifiedInner.equals( outerReplace ) )
		{
			return simplifiedOuter;
		}

		return new Chain( simplifiedOuter, simplifiedInner, outerReplace );
	}

	@Override
	public boolean contains( Therm str )
	{
		return inner.contains( str ) && outer.contains( outerReplace );
	}

	@Override
	public boolean equals( Object obj )
	{
		if ( super.equals( obj ) ) return true;
		if ( !(obj instanceof Chain) ) return false;

		Chain other = (Chain) obj;
		return inner.equals( other.inner ) && outer.equals( other.outer );
	}

	@Override
	public void toString( ThermStringify builder )
	{
		if ( true )
		{
			builder.append( outer.toString().replaceAll( "(?<=[^A-Za-z]|^)" + outerReplace + "(?=[^A-Za-z]|$)",
					inner.toString() ) );
		}
		else
		{
			builder.append( "{" + outer.toString().replaceAll( "(?<=[^A-Za-z]|^)" + outerReplace + "(?=[^A-Za-z]|$)",
					inner.toString() ) + "}" );
		}

	}

	@Override
	public int getLevel()
	{
		return FUNCTION_LEVEL;
	}
}
