package android.view.ext;

/**
 * Linearly distributes satellites in the given total degree.
 * 
 * @author Siyamed SINIR
 *
 */
public class LinearDegreeProvider implements IDegreeProvider {
	public float[] getDegrees(int count, float totalDegrees){
		if(count < 1){
            return new float[]{};
        }
        
        if(count == 1){
            return new float[]{45};
        }
        
        float[] result = null;
        int tmpCount = count-1;
        
        result = new float[count];
        float delta = totalDegrees / tmpCount;
        
        for(int index=0; index<count; index++){
            int tmpIndex = index;
            result[index] = tmpIndex * delta;
        }
        
        return result;
	}
}
