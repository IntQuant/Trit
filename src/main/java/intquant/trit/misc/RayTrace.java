package intquant.trit.misc;

import java.util.HashSet;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class RayTrace {

	public RayTrace() {
		// TODO Auto-generated constructor stub
	}
	//http://www.minecraftforge.net/forum/topic/8454-ray-tracing-from-an-entitys-head-angle/
	public static RayTraceResult tracePath(World world, float x, float y, float z, float tx, float ty, float tz, float borderSize, @Nullable HashSet<Entity> excluded)
	  {
	  Vec3d startVec = new Vec3d(x, y, z);
	  Vec3d lookVec = new Vec3d(tx-x, ty-y, tz-z);
	  Vec3d endVec = new Vec3d(tx, ty, tz);
	  float minX = x < tx ? x : tx;
	  float minY = y < ty ? y : ty;
	  float minZ = z < tz ? z : tz;
	  float maxX = x > tx ? x : tx;
	  float maxY = y > ty ? y : ty; 
	  float maxZ = z > tz ? z : tz;
	  AxisAlignedBB bb = new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ).expand(borderSize, borderSize, borderSize);
	  List<Entity> allEntities = world.getEntitiesWithinAABBExcludingEntity(null, bb);  
	  RayTraceResult blockHit = world.rayTraceBlocks(startVec, endVec);
	  startVec = new Vec3d(x, y, z);
	  endVec = new Vec3d(tx, ty, tz);
	  //float maxDistance = (float) endVec.distanceTo(startVec);
	  //if(blockHit!=null)
	  //  {
	  //  maxDistance = (float) blockHit.hitVec.distanceTo(startVec);
	  //  }  
	  Entity closestHitEntity = null;
	  float closestHit = Float.POSITIVE_INFINITY;
	  float currentHit = 0.f;
	  AxisAlignedBB entityBb;// = ent.getBoundingBox();
	  RayTraceResult intercept;
	  for(Entity ent : allEntities)
	    {    
	    if(ent.canBeCollidedWith() && (excluded==null || !excluded.contains(ent)))
	      {
	      float entBorder =  ent.getCollisionBorderSize();
	      entityBb = ent.getEntityBoundingBox();
	      if(entityBb!=null)
	        {
	        entityBb = entityBb.expand(entBorder, entBorder, entBorder);
	        intercept = entityBb.calculateIntercept(startVec, endVec);
	        if(intercept!=null)
	          {
	          currentHit = (float) intercept.hitVec.distanceTo(startVec);
	          if(currentHit < closestHit || currentHit==0)
	            {            
	            closestHit = currentHit;
	            closestHitEntity = ent;
	            }
	          } 
	        }
	      }
	    }  
	  if(closestHitEntity!=null)
	    {
	    blockHit = new RayTraceResult(closestHitEntity);
	    }
	  return blockHit;
	  }

	public static RayTraceResult tracePath(World world, double x, double y, double z, double x2, double y2, double z2,
			double d, @Nullable HashSet<Entity> excluded) {
		// TODO Auto-generated method stub
		return tracePath(world, (float)x, (float)y, (float)z, (float)x2, (float)y2, (float)z2, (float)d, excluded); 		
	}
	
	public static RayTraceResult tracePath(World world, Vec3d startPos, Vec3d endPos,
			double d, @Nullable HashSet<Entity> excluded) {
		return tracePath(world, startPos.x, startPos.y, startPos.z, endPos.x, endPos.y, endPos.z, d, excluded);
	}


}
