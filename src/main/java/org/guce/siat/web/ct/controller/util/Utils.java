package org.guce.siat.web.ct.controller.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.guce.siat.common.model.Administration;
import org.guce.siat.common.model.Bureau;
import org.guce.siat.common.model.Entity;
import org.guce.siat.common.model.File;
import org.guce.siat.common.model.FileItem;
import org.guce.siat.common.model.User;
import org.guce.siat.common.model.UserAuthorityFileType;
import org.guce.siat.common.service.FileItemService;
import org.guce.siat.common.service.UserAuthorityFileTypeService;
import org.guce.siat.common.utils.SiatUtils;
import org.guce.siat.common.utils.enums.InformationSystemCode;
import org.guce.siat.core.ct.model.Laboratory;
import org.guce.siat.core.ct.model.TreatmentCompany;

/**
 *
 * @author tadzotsa
 */
public class Utils {

    public final static String COTONPRODUCTTYPE = "COTON";

    public static Map<String, String> getProductTypePackaging() {

        Map<String, String> map = new HashMap<>();

        map.put("CF", "SACS");
        map.put("CC", "SACS");
        map.put("GR", "BILLES");
        map.put("BT", "COLIS");
        map.put("OA", "PIECES");
        map.put("PS", "COLIS");
        map.put("COTON", "BALLES");

        return map;
    }

    public static List<String> getCacaProductsTypes() {
        return Arrays.asList("CF", "CC");
    }

    public static List<String> getWoodProductsTypes() {
        return Arrays.asList("GR", "BT", "OA", "PS");
    }

    public static List<FileItem> getItems(UserAuthorityFileTypeService userAuthorityFileTypeService, FileItemService fileItemService, User loggedUser, List<UserAuthorityFileType> listUserAuthorityFileTypes) {

        if (Objects.equals(listUserAuthorityFileTypes, null)) {
            listUserAuthorityFileTypes = userAuthorityFileTypeService.findUserAuthorityFileTypeByUserList(loggedUser.getMergedDelegatorList());
        }

        // Merge the logged user and their delegator users list in the list
        Set<Administration> adminList = new HashSet<>();

        if (loggedUser.getMergedDelegatorList() != null) {
            for (final User user : loggedUser.getMergedDelegatorList()) {
                if (user.getAdministration() instanceof Laboratory || user.getAdministration() instanceof TreatmentCompany) {
                    adminList.add(((Entity) user.getAdministration()).getService());
                }
                adminList.add(user.getAdministration());
            }
        }
        // get the bureaus  for the administration of the logged user and their delegator users
        List<Bureau> bureauList = SiatUtils.findCombinedBureausByAdministrationList(new ArrayList<>(adminList));

        List<FileItem> items = fileItemService.findFileItemByServiceAndAuthoritiesAndFileType(bureauList, loggedUser, InformationSystemCode.CCT, listUserAuthorityFileTypes);

        return items;
    }

    /**
     * Extract files form items.
     *
     * @param items the items
     * @return the sets the
     */
    public static Set<File> extractFilesFormItems(Collection<FileItem> items) {

        Set<File> files = new HashSet<>();

        for (FileItem item : items) {
            files.add(item.getFile());
        }

        return files;
    }

    private Utils() {
    }

}
