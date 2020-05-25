/**   
 * @Title: StaticSysParam.java    
 * @Package com.game.domain.s    
 * @Description: TODO  
 * @author WanYi  
 * @date 2016年5月18日 下午1:30:50    
 * @version V1.0   
 */
package config.bean;

import java.util.List;

import config.IConfigBean;

/**
 * @ClassName: StaticSysParam
 * @Description: TODO
 * @author WanYi
 * @date 2016年5月18日 下午1:30:50
 * 
 */
public class ConfMilitaryBless implements IConfigBean{
	private List<List<Integer>> awardOne;

	private int weight;

	public List<List<Integer>> getAwardOne() {
		return awardOne;
	}

	public void setAwardOne(List<List<Integer>> awardOne) {
		this.awardOne = awardOne;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

}
