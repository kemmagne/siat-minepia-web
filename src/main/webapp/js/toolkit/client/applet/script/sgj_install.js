function Jar(name, version)
{
	this.name = name;
	this.version = version;
}

var installFunction = 
{
	getList : function(array)
	{
		var list = { jarlist: '',verlist: ''};
		for(var index in array)
		{
			list.jarlist = list.jarlist + array[index].name + ',';
			list.verlist = list.verlist + array[index].version + ',';
		}
		list.jarlist = list.jarlist.substr(0, list.jarlist.length -1);
		list.verlist = list.verlist.substr(0, list.verlist.length -1);
		return list;
	},
	
	setKeyProtect : function (keyProtect)
	{
		if(keyProtect == 'INCA' || keyProtect == 'INCA_KEYPAD')
		{
			libs.push(new Jar('inca.jar', '2.0.0.0'));
			libs.push(new Jar('inca_keypad.jar', '2.0.0.0'));
		}
		else if(keyProtect == 'AHN')
		{
			libs.push(new Jar('aos.jar', '2.0.0.0'));
			libs.push(new Jar('jna.jar', '2.0.0.0'));
			libs.push(new Jar('jna-3.2.1.jar', '2.0.0.0'));
		}
		/*
		else if(keyProtect == 'INCA_KEYPAD')
			libs.push(new Jar('inca_keypad.jar', '2.0.0.0'));
		*/
		return keyProtect;
	}
};

function getDocumentCharset()
{
	return document.characterSet ? document.characterSet : document.charset;
}

var libs = 
[
	new Jar('sgjclient.jar', '2.0.0.0'),
];


var libs2 = 
[
	new Jar('ebidclient.jar', '2.0.0.0'),
];

var dlls = 
[
	"BHSM_JNI2.dll-2.0.0.1",
	//"KICAUAC.dll-1.0.0.3",
	//"KicaUACJni.dll-1.0.0.2",
	//"gpkiapi2.dll-1.5.1.0",
	//"gpkiapi.dll-1.2.0.0",
	//"nsldap32v11.dll-4224.0.0.8803"
];

var object_id = 'SG_OpenWebKit';
var sessionId = 'localhost';

loadKICAApplet('DEFAULT', '');

function loadKICAApplet(mode, keyProtect)
{
	var attributes = {};
	var parameters = {};
	
	//setting attribute
	attributes.codebase = toolkitPath + '/client/applet/cab/';
	
	attributes.id = object_id;
	if(navigator.userAgent.indexOf("Linux") != -1)
	{ attributes.width = 280; attributes.height = 250; }
	else { attributes.width = 280; attributes.height = 250; }

	//setting parameters
	parameters.charset = getDocumentCharset();
	parameters.locale = 'en_CM';
	//parameters.locale = 'ko_KR'; 
	parameters.callBack = ''; 
	parameters.nativeLib = dlls.join(','); //dll library
	//parameters.keyProtect = ""; //installFunction.setKeyProtect(keyProtect); 
	parameters.java_arguments = '-Xmx512m -Djnlp.packEnabled=true';
	parameters.separate_jvm = 'true';
	parameters.MAYSCRIPT = 'true';
	parameters.codebase_lookup = 'false';
	parameters.scriptable = 'true';
	
	//parameters.infovineParam = 'CHANNELNAME:PTBANK_BANKTOWN;CERT_COMPANY:SIGNGATE;KEYCRYP_COMPANY:INCA;VIRTUALKEY_COMPANY:INCA;BROWSER_KEYTYPE:IEONLY_KEYCRYP_AND_NONIE_VIRTUALKEY';
	
	switch(mode)
	{
		case 'DEFAULT':
			attributes.code = 'com.kica.jclient.launcher.LauncherApplet';
			break;
		case 'BID':
			attributes.code = 'com.kica.ebid.jclient.launcher.LauncherAppletBid';
			break;
		default:
			attributes.code = 'com.kica.jclient.launcher.LauncherApplet';
			break;
	}
	
	switch(mode)
	{
		case 'DEFAULT':
			var list = installFunction.getList(libs);
			break;
		case 'BID':
			var list = installFunction.getList(libs2);
			break;
		default:
			var list = installFunction.getList(libs);
			break;
	}

	parameters.cache_archive = list.jarlist;
	parameters.cache_version = list.verlist;
	//run applet
	deployJava.runApplet(attributes, parameters, '1.6');
}
