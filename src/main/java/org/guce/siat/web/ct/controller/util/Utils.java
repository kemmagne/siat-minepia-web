package org.guce.siat.web.ct.controller.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.guce.siat.common.model.Administration;
import org.guce.siat.common.model.Bureau;
import org.guce.siat.common.model.Entity;
import org.guce.siat.common.model.File;
import org.guce.siat.common.model.FileFieldValue;
import org.guce.siat.common.model.FileItem;
import org.guce.siat.common.model.Params;
import org.guce.siat.common.model.Step;
import org.guce.siat.common.model.User;
import org.guce.siat.common.model.UserAuthorityFileType;
import org.guce.siat.common.service.FileFieldValueService;
import org.guce.siat.common.service.FileItemService;
import org.guce.siat.common.service.FileTypeStepService;
import org.guce.siat.common.service.ParamsService;
import org.guce.siat.common.service.UserAuthorityFileTypeService;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.common.utils.SiatUtils;
import org.guce.siat.common.utils.enums.FileTypeCode;
import org.guce.siat.common.utils.enums.InformationSystemCode;
import org.guce.siat.common.utils.enums.StepCode;
import org.guce.siat.core.ct.model.Laboratory;
import org.guce.siat.core.ct.model.TreatmentCompany;
import org.guce.siat.core.ct.service.CommonService;
import org.guce.siat.core.ct.service.MinaderStatisticsService;
import org.guce.siat.core.ct.util.enums.CctExportProductType;
import org.guce.siat.web.common.util.WebConstants;

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
     * @param fileTypeStepService
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

    public static Set<File> getFilesSet(FileTypeStepService fileTypeStepService,
            UserAuthorityFileTypeService userAuthorityFileTypeService,
            FileItemService fileItemService,
            CommonService commonService,
            FileFieldValueService fileFieldValueService,
            User loggedUser) {
        List<UserAuthorityFileType> listUserAuthorityFileTypes = userAuthorityFileTypeService.findUserAuthorityFileTypeByUserList(loggedUser.getMergedDelegatorList());
        List<FileItem> fileItems = getItems(userAuthorityFileTypeService, fileItemService, loggedUser, listUserAuthorityFileTypes);

        Set<File> filesSet = new HashSet<>(extractFilesFormItems(fileItems));

        List<CctExportProductType> userProductTypes = commonService.findProductTypesByUser(loggedUser);
        for (Iterator<File> iterator = filesSet.iterator(); iterator.hasNext();) {

            File file = iterator.next();
            FileItem fileItem = file.getFileItemsList().get(0);
            file.setRedefinedLabelEn(fileItem.getRedefinedLabelEn());
            file.setRedefinedLabelFr(fileItem.getRedefinedLabelFr());
            Step step = fileItem.getStep();
            file.setStep(step);

            if (!isPhyto(file) || Arrays.asList(StepCode.ST_CT_57, StepCode.ST_CT_60, StepCode.ST_CT_03, StepCode.ST_CT_47, StepCode.ST_CT_53, StepCode.ST_CT_62).contains(fileItem.getStep().getStepCode())) {
                continue;
            }

            if (MinaderStatisticsService.TREATMENT_STEPS_CODES.contains(fileItem.getStep().getStepCode()) && !loggedUser.equals(file.getAssignedUser())) {
                iterator.remove();
                continue;
            }

            FileFieldValue ffv = fileFieldValueService.findValueByFileFieldAndFile(CctExportProductType.getFileFieldCode(), file);
            if (ffv == null) {
                continue;
            }

            CctExportProductType productType = CctExportProductType.valueOf(ffv.getValue());
            if (!userProductTypes.contains(productType)) {
                iterator.remove();
            }
        }

        return filesSet;
    }

    public static boolean isPhyto(File currentFile) {
        boolean checkMinaderMinistry = currentFile.getDestinataire().equalsIgnoreCase(Constants.MINADER_MINISTRY);
        return checkMinaderMinistry && Arrays.asList(FileTypeCode.CCT_CT_E, FileTypeCode.CCT_CT_E_ATP, FileTypeCode.CCT_CT_E_FSTP, FileTypeCode.CCT_CT_E_PVE, FileTypeCode.CCT_CT_E_PVI).contains(currentFile.getFileType().getCode());
    }

    public static boolean canViewAnyFile(ParamsService paramsService, User loggedUser) {
        Params params = paramsService.findParamsByName("user.can.view.any.file");
        if (params == null) {
            return false;
        }
        String logins = params.getValue();
        if (StringUtils.isBlank(logins)) {
            return false;
        }
        return Arrays.asList(logins.split(";")).contains(loggedUser.getLogin());
    }

    public static String getFinalDetailPageUrl(File file, String indexPage, boolean redirect, boolean search) {
        String finalPageUrl = indexPage;
        finalPageUrl = finalPageUrl.concat("?");
        if (redirect) {
            finalPageUrl = finalPageUrl.concat("faces-redirect=true&");
        }
        finalPageUrl = finalPageUrl.concat(String.format("%s=%s",
                WebConstants.FILE_NUMBER_REQUEST_PARAM_KEY,
                file.getNumeroDossier())
        );
        if (search) {
            finalPageUrl = finalPageUrl.concat(String.format("&%s=%s", WebConstants.SEARCH_BOOLEAN_REQUEST_PARAM_KEY, true));
        }
        return finalPageUrl;
    }

    private Utils() {
    }

}
