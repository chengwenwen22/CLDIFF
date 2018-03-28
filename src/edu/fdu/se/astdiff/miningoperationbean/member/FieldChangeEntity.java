package edu.fdu.se.astdiff.miningoperationbean.member;

import com.github.gumtreediff.actions.model.Move;
import edu.fdu.se.astdiff.linkpool.LinkBean;
import edu.fdu.se.astdiff.linkpool.MyRange;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;
import edu.fdu.se.astdiff.miningoperationbean.base.ChangeEntity;
import edu.fdu.se.astdiff.miningoperationbean.base.ChangeEntityDesc;
import edu.fdu.se.astdiff.miningoperationbean.base.MemberPlusChangeEntity;
import edu.fdu.se.astdiff.preprocessingfile.BodyDeclarationPair;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import java.util.List;

/**
 * Created by huangkaifeng on 2018/1/16.
 *
 */
public class FieldChangeEntity extends MemberPlusChangeEntity {



    /**
     * 预处理识别的
     */
    public FieldChangeEntity(BodyDeclarationPair fieldDeclarationPair, int changeType,MyRange myRange){
        super(fieldDeclarationPair.getLocationClassString(),changeType,myRange);
        FieldDeclaration fd = (FieldDeclaration) fieldDeclarationPair.getBodyDeclaration();
        this.stageIIBean.setLocation(fieldDeclarationPair.getLocationClassString());
        this.stageIIBean.setChangeEntity(ChangeEntityDesc.StageIIIENTITY.ENTITY_FIELD);

        List<VariableDeclarationFragment> list = fd.fragments();
        String res = "";
        for(VariableDeclarationFragment vd:list){
            res += vd+",";
            this.linkBean.variables.add(vd.getName().toString());
        }
        this.stageIIBean.setThumbnail(fieldDeclarationPair.getLocationClassString() + res);

    }

    /**
     * gumtree 识别的
     * @param bean
     */
    public FieldChangeEntity(ClusteredActionBean bean,String granularity){
        super(bean);
        this.lineRange = bean.range;
        this.stageIIBean.setChangeEntity(ChangeEntityDesc.StageIIIENTITY.ENTITY_FIELD);
        this.stageIIBean.setGranularity(ChangeEntityDesc.StageIIGranularity.GRANULARITY_MEMBER);

        this.stageIIBean.setOpt(OperationTypeConstants.getKeyNameByValue(bean.changePacket.getOperationType()));
        if(bean.curAction instanceof Move){
            this.linkBean = new LinkBean(bean.curAction);
        }else {
            this.linkBean = new LinkBean(bean.actions);
        }
    }





}
