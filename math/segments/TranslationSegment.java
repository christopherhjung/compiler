package segments;

import vector.Matrix4f;
import vector.ReadableVector3f;
import vector.Vector3f;

public class TranslationSegment extends Segment{

	private float length = 0;
	private boolean dirty = true;
	private Vector3f translation;
	
	public TranslationSegment( Segment child , ReadableVector3f translation ){
		super(child);
		this.translation = new Vector3f(translation);
		this.translation.normalise();
	}
	
	public void setLength( float length ){
		this.length = length;
		this.dirty = true;
	}
	
	@Override
	protected boolean isDirty() {
		return dirty;
	}

	@Override
	public Matrix4f computeTransformation(Matrix4f convertMatrix) {
		dirty = false;
		convertMatrix.translate(Vector3f.scale(translation,length,null));
		
		return convertMatrix;
	}
	
}
