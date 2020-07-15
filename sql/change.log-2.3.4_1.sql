/**
 * Author:  ht
 * Created: 13 juil. 2020
 */

-- MINADER_FILES
CREATE OR REPLACE FORCE VIEW "SIAT_CT"."MINADER_FILES" (
    "ID",
    "CREATED_DATE",
    "DESTINATAIRE",
    "EMETTEUR",
    "FILE_TYPE_GUCE",
    "FILE_TYPE_GUCE_ANNULATION",
    "FILE_TYPE_GUCE_PAIEMENT",
    "NUMERO_DEMANDE",
    "NUMERO_DEMANDE_ANNULATION",
    "NUMERO_DEMANDE_PAIEMENT",
    "NUMERO_DOSSIER",
    "REFERENCE_GUCE",
    "REFERENCE_GUCE_ANNULATION",
    "REFERENCE_GUCE_PAIEMENT",
    "REFERENCE_SIAT",
    "ASSIGNED_USER_ID",
    "BUREAU_ID",
    "CLIENT_ID",
    "COUNTRY_OF_DESTINATION",
    "COUNTRY_OF_ORIGIN",
    "COUNTRY_OF_PROVENANCE",
    "FILE_TYPE_ID",
    "FILE_TYPE_CODE",
    "FILE_TYPE_NAME_FR",
    "FILE_TYPE_NAME_EN",
    "SIGNATURE_DATE",
    "VALIDITY_DATE",
    "SIGNATORY_USER_ID",
    "LAST_DECISION_DATE",
    "TYPE_OPERATION",
    "TYPE_PRODUIT_CODE",
    "TYPE_PRODUIT_NOM",
    "DATE_REJET",
    "CODE_BUREAU",
    "NOM_BUREAU",
    "FILE_TYPE_NAME",
    "TRANSITAIRE_NOM"
) AS
    SELECT DISTINCT
        f."ID",
        f."CREATED_DATE",
        f."DESTINATAIRE",
        f."EMETTEUR",
        f."FILE_TYPE_GUCE",
        f."FILE_TYPE_GUCE_ANNULATION",
        f."FILE_TYPE_GUCE_PAIEMENT",
        f."NUMERO_DEMANDE",
        f."NUMERO_DEMANDE_ANNULATION",
        f."NUMERO_DEMANDE_PAIEMENT",
        f."NUMERO_DOSSIER",
        f."REFERENCE_GUCE",
        f."REFERENCE_GUCE_ANNULATION",
        f."REFERENCE_GUCE_PAIEMENT",
        f."REFERENCE_SIAT",
        f."ASSIGNED_USER_ID",
        f."BUREAU_ID" "ID_BUREAU",
        f."CLIENT_ID",
        f."COUNTRY_OF_DESTINATION",
        f."COUNTRY_OF_ORIGIN",
        f."COUNTRY_OF_PROVENANCE",
        f."FILE_TYPE_ID",
        ft.CODE       FILE_TYPE_CODE,
        ft.LABEL_FR   FILE_TYPE_NAME_FR,
        ft.LABEL_EN   FILE_TYPE_NAME_EN,
        nvl2(
            f.signature_date,
            f.last_decision_date,
            f.signature_date
        ) AS "SIGNATURE_DATE",
        f."VALIDITY_DATE",
        f."SIGNATORY_USER_ID",
        f."LAST_DECISION_DATE",
        (
            SELECT
                MAX(ffv.value)
            FROM
                file_field_value ffv
                JOIN file_field ff ON ff.id = ffv.file_field_id
            WHERE
                    ffv.file_id = f.id
                AND
                    ff.code = 'TYPE_DOSSIER_EGUCE'
        ) type_operation,
        (
            SELECT
                MAX(ffv.value)
            FROM
                file_field_value ffv
                JOIN file_field ff ON ff.id = ffv.file_field_id
            WHERE
                    ffv.file_id = f.id
                AND
                    ff.code = 'TYPE_PRODUIT_CODE'
        ) type_produit_code,
        (
            SELECT
                MAX(ffv.value)
            FROM
                file_field_value ffv
                JOIN file_field ff ON ff.id = ffv.file_field_id
            WHERE
                    ffv.file_id = f.id
                AND
                    ff.code = 'TYPE_PRODUIT_NOM'
        ) type_produit_nom,
        (
            SELECT
                MIN(itf.created)
            FROM
                file_item fi
                JOIN item_flow itf ON itf.file_item_id = fi.id
                JOIN flow flow ON flow.id = itf.flow_id
            WHERE
                    fi.file_id = f.id
                AND
                    flow.to_step = 5
        ) date_rejet,
        ent.code code_bureau,
        adm.label_fr nom_bureau,
        ft.label_fr file_type_name,
        (
            SELECT
                MAX(ffv.value)
            FROM
                file_field_value ffv
                JOIN file_field ff ON ff.id = ffv.file_field_id
            WHERE
                    ffv.file_id = f.id
                AND
                    ff.code = 'TRANSITAIRE_RAISONSOCIALE'
        ) transitaire_nom
    FROM
        files f
        JOIN file_type ft ON ft.id = f.file_type_id and ft.code IN ('CCT_CT_E_PVI','CCT_CT_E_PVE','CCT_CT_E_FSTP','CCT_CT_E_ATP','CCT_CT_E')
        LEFT JOIN entity ent ON ent.id = f.bureau_id
        LEFT JOIN administration adm ON adm.id = f.bureau_id
    WHERE
        f."BUREAU_ID" IS NOT NULL;

-- MINADER_FILE_ITEM
CREATE OR REPLACE FORCE VIEW "SIAT_CT"."MINADER_FILE_ITEM" (
    "ID",
    "DRAFT",
    "VALEUR_FOB",
    "FICTIVE",
    "LINE_NUMBER",
    "NUM_EBMS_MSG",
    "NUM_EBMS_MSG_ANNULATION",
    "NUM_EBMS_MSG_PAIEMENT",
    "QUANTITY",
    "FILE_ID",
    "NSH_ID",
    "STEP_ID",
    "SUBFAMILY_ID",
    "GOODS_ITEM_DESC",
    "NOMBRE_GRUMES",
    "NOMBRE_SACS",
    "VOLUME",
    "POIDS_BRUT",
    "POIDS_NET"
) AS
    SELECT
        fi."ID",
        fi."DRAFT",
        TO_NUMBER(fi."VALEUR_FOB") VALEUR_FOB,
        fi."FICTIVE",
        fi."LINE_NUMBER",
        fi."NUM_EBMS_MSG",
        fi."NUM_EBMS_MSG_ANNULATION",
        fi."NUM_EBMS_MSG_PAIEMENT",
        TO_NUMBER(fi."QUANTITY") QUANTITY,
        fi."FILE_ID",
        fi."NSH_ID",
        fi."STEP_ID",
        fi."SUBFAMILY_ID",
        n.goods_item_desc,
        (
            SELECT
                TO_NUMBER(MAX(fifv.value))
            FROM
                file_item_field_value fifv
                JOIN file_item_field fif ON fif.id = fifv.file_item_field_id
            WHERE
                    fifv.file_item_id = fi.id
                AND
                    fif.code = 'NOMBRE_GRUMES'
        ) nombre_grumes,
        (
            SELECT
                TO_NUMBER(MAX(fifv.value))
            FROM
                file_item_field_value fifv
                JOIN file_item_field fif ON fif.id = fifv.file_item_field_id
            WHERE
                    fifv.file_item_id = fi.id
                AND
                    fif.code = 'NOMBRE_SACS'
        ) nombre_sacs,
        (
            SELECT
                TO_NUMBER(MAX(fifv.value))
            FROM
                file_item_field_value fifv
                JOIN file_item_field fif ON fif.id = fifv.file_item_field_id
            WHERE
                    fifv.file_item_id = fi.id
                AND
                    fif.code = 'VOLUME'
        ) volume,
        (
            SELECT
                TO_NUMBER(MAX(fifv.value))
            FROM
                file_item_field_value fifv
                JOIN file_item_field fif ON fif.id = fifv.file_item_field_id
            WHERE
                    fifv.file_item_id = fi.id
                AND
                    fif.code = 'POIDS_BRUT'
        ) poids_brut,
        (
            SELECT
                TO_NUMBER(MAX(fifv.value))
            FROM
                file_item_field_value fifv
                JOIN file_item_field fif ON fif.id = fifv.file_item_field_id
            WHERE
                    fifv.file_item_id = fi.id
                AND
                    fif.code = 'POIDS'
        ) poids_net
    FROM
        file_item fi
        JOIN rep_nsh n ON fi.nsh_id = n.goods_item_code
        JOIN files f ON fi.file_id = f.id AND f."BUREAU_ID" IS NOT NULL AND f.file_type_id IN (
            SELECT ID FROM FILE_TYPE WHERE CODE IN ('CCT_CT_E_PVI','CCT_CT_E_PVE','CCT_CT_E_FSTP','CCT_CT_E_ATP','CCT_CT_E')
        );

-- MINADER_FILES_TRACKING
CREATE OR REPLACE FORCE VIEW SIAT_CT.MINADER_FILES_TRACKING AS
    SELECT
        F.NUMERO_DEMANDE,
        F.NUMERO_DOSSIER,
        FT.CODE       FILE_TYPE_CODE,
        FT.LABEL_FR   FILE_TYPE_NAME_FR,
        FT.LABEL_EN   FILE_TYPE_NAME_EN,
        F.CREATED_DATE,
        C.NUM_CONTRIBUABLE,
        C.COMPANY_NAME,
        TPC.VALUE     "TYPE_PRODUIT_CODE",
        TPN.VALUE     "TYPE_PRODUIT_NOM",
        E.ID          BUREAU_ID,
        E.CODE        BUREAU_CODE,
        AD.LABEL_FR   BUREAU_NAME_FR,
        AD.LABEL_EN   BUREAU_NAME_EN
    FROM
        FILES              F
        JOIN FILE_TYPE          FT ON FT.ID = F.FILE_TYPE_ID AND FT.CODE IN (
            'CCT_CT_E_PVI',
            'CCT_CT_E_PVE',
            'CCT_CT_E_FSTP',
            'CCT_CT_E_ATP',
            'CCT_CT_E'
        )
        JOIN COMPANY            C ON C.ID = F.CLIENT_ID
        JOIN FILE_FIELD         TPCFF ON TPCFF.FILE_TYPE_ID = FT.ID
                                 AND TPCFF.CODE = 'TYPE_PRODUIT_CODE'
        JOIN FILE_FIELD_VALUE   TPC ON TPC.FILE_FIELD_ID = TPCFF.ID
                                     AND TPC.FILE_ID = F.ID
        JOIN FILE_FIELD         TPNFF ON TPNFF.FILE_TYPE_ID = FT.ID
                                 AND TPNFF.CODE = 'TYPE_PRODUIT_NOM'
        JOIN FILE_FIELD_VALUE   TPN ON TPN.FILE_FIELD_ID = TPNFF.ID
                                     AND TPN.FILE_ID = F.ID
        JOIN ENTITY             E ON E.ID = F.BUREAU_ID
        JOIN ADMINISTRATION     AD ON AD.ID = F.BUREAU_ID
    WHERE
        F.BUREAU_ID IS NOT NULL
        AND F.NUMERO_DEMANDE IS NOT NULL
    ORDER BY
        F.ID DESC;

