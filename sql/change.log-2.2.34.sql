/**
 * Author:  tadzotsa
 * Created: 28 janv. 2020
 */
UPDATE DATA_TYPE SET DISABLED = 1 WHERE FLOW_ID = (SELECT ID FROM FLOW WHERE CODE = 'FL_AP_104') AND LABEL <> 'Observation';
COMMIT;
