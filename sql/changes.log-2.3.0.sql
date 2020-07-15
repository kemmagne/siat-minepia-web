/**
 * Author:  ayefou
 * Created: 14 mai 2020
 */
CREATE OR REPLACE FUNCTION TO_NUMBER2(str VARCHAR2) RETURN NUMBER AS
    res number(38,4);
    dotCaracter VARCHAR2(10);
    strToConvert VARCHAR2(500);
BEGIN
    select SUBSTRB(value,1,1) into dotCaracter from v$nls_parameters where parameter = 'NLS_NUMERIC_CHARACTERS';
    IF dotCaracter IS NULL THEN strToConvert := str;
    ELSIF INSTR(str, '.') > 0 THEN strToConvert := REPLACE(str,'.',dotCaracter);
    ELSIF INSTR(str, ',') > 0 THEN strToConvert := REPLACE(str,',',dotCaracter);
    ELSE strToConvert := str;
    END IF;
    res := TO_NUMBER(strToConvert);
    RETURN res;
END;
/

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
        f."BUREAU_ID",
        f."CLIENT_ID",
        f."COUNTRY_OF_DESTINATION",
        f."COUNTRY_OF_ORIGIN",
        f."COUNTRY_OF_PROVENANCE",
        f."FILE_TYPE_ID",
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
        JOIN file_type ft ON ft.id = f.file_type_id
        LEFT JOIN entity ent ON ent.id = f.bureau_id
        LEFT JOIN administration adm ON adm.id = f.bureau_id
    WHERE
        f.file_type_id IN (
            33,36,37,39
        );


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
        TO_NUMBER2(fi."VALEUR_FOB") VALEUR_FOB,
        fi."FICTIVE",
        fi."LINE_NUMBER",
        fi."NUM_EBMS_MSG",
        fi."NUM_EBMS_MSG_ANNULATION",
        fi."NUM_EBMS_MSG_PAIEMENT",
        TO_NUMBER2(fi."QUANTITY") QUANTITY,
        fi."FILE_ID",
        fi."NSH_ID",
        fi."STEP_ID",
        fi."SUBFAMILY_ID",
        n.goods_item_desc,
        (
            SELECT
                TO_NUMBER2(MAX(fifv.value))
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
                TO_NUMBER2(MAX(fifv.value))
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
                TO_NUMBER2(MAX(fifv.value))
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
                TO_NUMBER2(MAX(fifv.value))
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
                TO_NUMBER2(MAX(fifv.value))
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
        JOIN files f ON fi.file_id = f.id
    WHERE
        f.file_type_id IN (
            33,36,37,39
        );
