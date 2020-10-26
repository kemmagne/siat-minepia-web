package org.guce.siat.web.ct.controller.helpers;

import java.io.Serializable;
import org.apache.commons.collections.CollectionUtils;
import org.guce.siat.common.data.ItemFlowDto;
import org.guce.siat.common.model.ItemFlow;
import org.guce.siat.common.model.Transfer;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.common.utils.enums.AperakType;

/**
 *
 * @author ht
 */
public class CctDetailControllerHelper implements Serializable {

    private static final long serialVersionUID = -2308625868655484859L;

    public ItemFlowDto transform(Transfer transfer) {

        if (transfer.getUser() == null || transfer.getAssignedUser() == null || Constants.SYSTEM_USER_LOGIN.equals(transfer.getUser().getLogin())) {
            return null;
        }

        ItemFlowDto itemFlowDto = new ItemFlowDto();

        ItemFlow itemFlow = new ItemFlow();

        itemFlow.setAssignedUser(transfer.getAssignedUser());
        itemFlow.setCreated(transfer.getCreatedDate());
        if (CollectionUtils.isNotEmpty(transfer.getFile().getFileItemsList())) {
            itemFlow.setFileItem(transfer.getFile().getFileItemsList().get(0));
        }
        itemFlow.setReceived(AperakType.APERAK_D.getCharCode());
        itemFlow.setSender(transfer.getUser());
        itemFlow.setSent(Boolean.TRUE);
        itemFlow.setUnread(Boolean.FALSE);
        itemFlow.setId(transfer.getId());

        itemFlowDto.setItemFlow(itemFlow);
        itemFlowDto.setDuration("-");

        return itemFlowDto;
    }

}
