package segments;
import vector.Matrix4f;
import vector.ReadableVector3f;

public class RotationSegment extends Segment{

	private float angle = 0;
	private boolean dirty = true; 
	
	public RotationSegment(){
	}
	
	public RotationSegment( Segment child , ReadableVector3f rotationAxis){
		super(child);
	}
	
	public void setAngle( float angle ){
		this.angle = (float)Math.toRadians(angle);
		dirty = true;
	}

	@Override
	public boolean isDirty(){
		return dirty; 
	}
	
	@Override
	public Matrix4f computeTransformation( Matrix4f convertMatrix ) {
		convertMatrix.rotate(angle, rotationAxis);
		dirty = false;
		return convertMatrix;
	}
	
	
	
}
