package segments;
import vector.Matrix4f;

public abstract class Segment {
	
	private Matrix4f relativeTransformation = new Matrix4f();
	private Matrix4f absoluteTransformation = new Matrix4f();
	private Segment parent = null;
	
	public Segment(){}
	
	public Segment( Segment parent ){
		this.parent = parent;
	}

	protected abstract boolean isDirty();
	
	public abstract Matrix4f computeTransformation(Matrix4f convertMatrix);
	
	public final Matrix4f getTransformation(){
		boolean relativeDirty = isDirty();
		
		if(relativeDirty){
			relativeTransformation.setIdentity();
			relativeTransformation = computeTransformation(relativeTransformation);			
		}
		
		if(parent == null){
			absoluteTransformation.load(relativeTransformation);
		}else if(relativeDirty || parent.isDirty()){
			Matrix4f.mul(
				parent.getTransformation(),
				relativeTransformation, 
				absoluteTransformation);
		}
		
		return absoluteTransformation;
	}
	
	public Segment getParent(){
		return parent;
	}
}
