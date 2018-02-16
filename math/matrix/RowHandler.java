package matrix;

import therms.Therm;

public interface RowHandler {
	void add( int fromRow, int toRow, Therm factor );

	void mul( int row, Therm factor );
}
