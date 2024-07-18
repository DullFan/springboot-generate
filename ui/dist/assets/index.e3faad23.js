import{p as e,a as l,d as a,r as o,g as t,b as u,c as r,e as n,w as d,F as s,o as i,f as p,h as c,i as m,u as v,j as f,k as b,l as h,m as g,E as y,n as w,q as V,s as _,t as k}from"./vendor.c43ab50b.js";!function(){const e=document.createElement("link").relList;if(!(e&&e.supports&&e.supports("modulepreload"))){for(const e of document.querySelectorAll('link[rel="modulepreload"]'))l(e);new MutationObserver((e=>{for(const a of e)if("childList"===a.type)for(const e of a.addedNodes)"LINK"===e.tagName&&"modulepreload"===e.rel&&l(e)})).observe(document,{childList:!0,subtree:!0})}function l(e){if(e.ep)return;e.ep=!0;const l=function(e){const l={};return e.integrity&&(l.integrity=e.integrity),e.referrerpolicy&&(l.referrerPolicy=e.referrerpolicy),"use-credentials"===e.crossorigin?l.credentials="include":"anonymous"===e.crossorigin?l.credentials="omit":l.credentials="same-origin",l}(e);fetch(e.href,l)}}(),e("data-v-7fc06162");const q={class:"toolbar flex items-center w-full"},L=g("SpringBoot-Generate"),U=[p("path",{d:"M511.6 76.3C264.3 76.2 64 276.4 64 523.5 64 718.9 189.3 885 363.8 946c23.5 5.9 19.9-10.8 19.9-22.2v-77.5c-135.7 15.9-141.2-73.9-150.3-88.9C215 726 171.5 718 184.5 703c30.9-15.9 62.4 4 98.9 57.9 26.4 39.1 77.9 32.5 104 26 5.7-23.5 17.9-44.5 34.7-60.8-140.6-25.2-199.2-111-199.2-213 0-49.5 16.3-95 48.3-131.7-20.4-60.5 1.9-112.3 4.9-120 58.1-5.2 118.5 41.6 123.2 45.3 33-8.9 70.7-13.6 112.9-13.6 42.4 0 80.2 4.9 113.5 13.9 11.3-8.6 67.3-48.8 121.3-43.9 2.9 7.7 24.7 58.3 5.5 118 32.4 36.8 48.9 82.7 48.9 132.3 0 102.2-59 188.1-200 212.9 23.5 23.2 38.1 55.4 38.1 91v112.5c0.8 9 0 17.9 15 17.9 177.1-59.7 304.6-227 304.6-424.1 0-247.2-200.4-447.3-447.5-447.3z",fill:"#737478"},null,-1)],x=g("生成 "),C=g("SQL生成 "),B=g("导出代码 "),S=g("配置数据库 "),P={class:"px-5 pt-5"},K={class:"dialog-footer"},F=g("取消"),N=g(" 确定 "),I={class:"dialog-footer"},E=g("取消"),R=g(" 确定 "),j={class:"dialog-footer"},D=g("取消"),$=g(" 确定 ");l();var G=a({setup(e){const l=o(!1),a=o(!1),g=o(!1),_=o({packageBase:"",fieldIgnoreList:"",author:"",delFlagFields:"",tablePrefix:!1,enabledLombok:!1,springBootGenerateVersion:3,sqlIp:"",ipPort:"",sqlName:"",sqlUsername:"",sqlPassword:"",databaseName:"",sqlStatement:"",exportLocal:""});function k(){window.open("https://github.com/DullFan/springboot-generate","_blank")}const G=o(!0),O=[{value:"2",label:"2"},{value:"3",label:"3"}],Q=[{value:"true",label:"true"},{value:"false",label:"false"}],A=[{value:"true",label:"true"},{value:"false",label:"false"}];let z=o([]);function T(){}function M(){l.value=!0}function H(){a.value=!0}function J(){g.value=!0}const W=t().appContext.config.globalProperties.$api;function X(){""===_.value.sqlName.trim()||""===_.value.sqlIp.trim()||""===_.value.sqlUsername.trim()||""===_.value.sqlPassword.trim()||""===_.value.ipPort.trim()?y({title:"Error",message:"请填写完整数据库数据",type:"error"}):(w({method:"post",url:`${W}/findAllSQLStructure`,data:_.value}).then((function(e){500==e.data.code?y({title:"Error",message:e.data.msg,type:"error"}):z.value=e.data.data})),a.value=!1)}function Y(){_.value.sqlStatement=_.value.sqlStatement.replace(/(\r\n|\n|\r)/gm,""),""===_.value.packageBase.trim()&&(_.value.packageBase="com.dullfan"),""===_.value.author.trim()&&(_.value.author="DullFan");_.value.sqlStatement.trim().toLowerCase().includes("create")?(w({method:"post",url:`${W}/generateCodeBySQL`,data:_.value,responseType:"blob"}).then((function(e){const l=window.URL.createObjectURL(new Blob([e.data])),a=document.createElement("a");a.href=l,a.setAttribute("download","code.zip"),document.body.appendChild(a),a.click(),document.body.removeChild(a),window.URL.revokeObjectURL(l)})).catch((e=>{console.error("下载文件失败:",e)})),a.value=!1):y({title:"Error",message:"SQL不包含Create",type:"error"})}function Z(){""===_.value.packageBase.trim()&&(_.value.packageBase="com.dullfan"),""===_.value.author.trim()&&(_.value.author="DullFan"),_.value.sqlStatement="",w({method:"post",url:`${W}/generateCodeByTableName`,data:_.value,responseType:"blob"}).then((function(e){const l=window.URL.createObjectURL(new Blob([e.data])),a=document.createElement("a");a.href=l,a.setAttribute("download","code.zip"),document.body.appendChild(a),a.click(),document.body.removeChild(a),window.URL.revokeObjectURL(l)})).catch((e=>{console.error("下载文件失败:",e)})),a.value=!1}function ee(){const e=window.location.origin;e.includes("localhost")||e.includes("127.0.0.1")?(""===_.value.packageBase.trim()&&(_.value.packageBase="com.dullfan"),""===_.value.author.trim()&&(_.value.author="DullFan"),""!==_.value.exportLocal.trim()?(w({method:"post",url:`${W}/exportLocalCode`,data:_.value}).then((function(e){console.log(e),500==e.data.code?y({title:"Error",message:e.data.msg,type:"error"}):(z.value=e.data.data,g.value=!1,y({title:"Success",message:e.data.msg,type:"success"}))})).catch((e=>{console.error("下载文件失败:",e)})),a.value=!1):y({title:"Error",message:"请填写完整数据",type:"error"})):y({title:"Error",message:"请在本地环境中使用",type:"error"})}function le(e){G.value=0==e.length,_.value.databaseName=e.map((e=>e.tableName)).join(",")}return(e,o)=>{const t=u("el-text"),y=u("el-header"),w=u("el-input"),W=u("el-form-item"),ae=u("el-option"),oe=u("el-select"),te=u("el-form"),ue=u("el-button"),re=u("el-row"),ne=u("el-table-column"),de=u("el-table"),se=u("el-main"),ie=u("el-container"),pe=u("el-dialog");return i(),r(s,null,[n(ie,{class:"layout-container-demo",style:{height:"100vh"}},{default:d((()=>[n(ie,null,{default:d((()=>[n(y,{class:"text-left el-header-custom w-full"},{default:d((()=>[p("div",q,[n(t,null,{default:d((()=>[L])),_:1}),p("svg",{onClick:k,class:"max-w-7 max-h-7 ms-auto svg-icon",viewBox:"0 0 1024 1024",version:"1.1",xmlns:"http://www.w3.org/2000/svg",width:"64",height:"64"},U)])])),_:1}),n(se,null,{default:d((()=>[p("div",null,[n(te,{model:_.value,ref:(e,l)=>{l.queryForm=e},inline:!0,class:"px-5 pt-5"},{default:d((()=>[n(W,{label:"包名",prop:"packageBase"},{default:d((()=>[n(w,{modelValue:_.value.packageBase,"onUpdate:modelValue":o[0]||(o[0]=e=>_.value.packageBase=e),placeholder:"例如: com.dullfan.hahah",clearable:"",onKeyup:c(T,["enter","native"])},null,8,["modelValue","onKeyup"])])),_:1}),n(W,{label:"忽略的字段",prop:"fieldIgnoreList"},{default:d((()=>[n(w,{modelValue:_.value.fieldIgnoreList,"onUpdate:modelValue":o[1]||(o[1]=e=>_.value.fieldIgnoreList=e),placeholder:"例如: id,status",clearable:"",onKeyup:c(T,["enter","native"])},null,8,["modelValue","onKeyup"])])),_:1}),n(W,{label:"作者",prop:"author"},{default:d((()=>[n(w,{modelValue:_.value.author,"onUpdate:modelValue":o[2]||(o[2]=e=>_.value.author=e),placeholder:"例如: DullFan",clearable:"",onKeyup:c(T,["enter","native"])},null,8,["modelValue","onKeyup"])])),_:1}),n(W,{label:"逻辑删除",prop:"delFlagFields"},{default:d((()=>[n(w,{modelValue:_.value.delFlagFields,"onUpdate:modelValue":o[3]||(o[3]=e=>_.value.delFlagFields=e),placeholder:"例如: del_flag",clearable:"",onKeyup:c(T,["enter","native"])},null,8,["modelValue","onKeyup"])])),_:1}),n(W,{label:"SpringBoot版本",prop:"springBootGenerateVersion"},{default:d((()=>[n(oe,{modelValue:_.value.springBootGenerateVersion,"onUpdate:modelValue":o[4]||(o[4]=e=>_.value.springBootGenerateVersion=e),placeholder:"Select",style:{width:"80px"}},{default:d((()=>[(i(),r(s,null,m(O,(e=>n(ae,{key:e.value,label:e.label,value:e.value},null,8,["label","value"]))),64))])),_:1},8,["modelValue"])])),_:1}),n(W,{label:"是否忽略前缀",prop:"tablePrefix"},{default:d((()=>[n(oe,{modelValue:_.value.tablePrefix,"onUpdate:modelValue":o[5]||(o[5]=e=>_.value.tablePrefix=e),placeholder:"Select",style:{width:"80px"}},{default:d((()=>[(i(),r(s,null,m(A,(e=>n(ae,{key:e.value,label:e.label,value:e.value},null,8,["label","value"]))),64))])),_:1},8,["modelValue"])])),_:1}),n(W,{label:"是否配置Lombok",prop:"enabledLombok"},{default:d((()=>[n(oe,{modelValue:_.value.enabledLombok,"onUpdate:modelValue":o[6]||(o[6]=e=>_.value.enabledLombok=e),placeholder:"Select",style:{width:"80px"}},{default:d((()=>[(i(),r(s,null,m(Q,(e=>n(ae,{key:e.value,label:e.label,value:e.value},null,8,["label","value"]))),64))])),_:1},8,["modelValue"])])),_:1})])),_:1},8,["model"])]),n(re,{class:"px-5"},{default:d((()=>[n(ue,{type:"primary",disabled:G.value,plain:"",onClick:Z,icon:v(V)},{default:d((()=>[x])),_:1},8,["disabled","icon"]),n(ue,{type:"primary",plain:"",icon:v(f),onClick:M},{default:d((()=>[C])),_:1},8,["icon"]),n(ue,{type:"warning",plain:"",icon:v(b),onClick:J},{default:d((()=>[B])),_:1},8,["icon"]),n(ue,{type:"success",plain:"",icon:v(h),onClick:H},{default:d((()=>[S])),_:1},8,["icon"])])),_:1}),p("div",P,[n(de,{data:v(z),"header-cell-style":{background:"#f8f8f9"},onSelectionChange:le},{default:d((()=>[n(ne,{align:"center",type:"selection",width:"55"}),n(ne,{align:"center",type:"index",label:"序号","min-width":"100"}),n(ne,{align:"center",prop:"tableName",label:"表名","min-width":"180"}),n(ne,{align:"center",prop:"beanName",label:"实体","min-width":"180"}),n(ne,{align:"center",prop:"comment",label:"表描述","min-width":"180"})])),_:1},8,["data"])])])),_:1})])),_:1})])),_:1}),n(pe,{modelValue:a.value,"onUpdate:modelValue":o[13]||(o[13]=e=>a.value=e),title:"配置数据库",width:"500"},{footer:d((()=>[p("div",K,[n(ue,{onClick:o[12]||(o[12]=e=>a.value=!1)},{default:d((()=>[F])),_:1}),n(ue,{type:"primary",onClick:X},{default:d((()=>[N])),_:1})])])),default:d((()=>[n(te,{model:_.value,ref:(e,l)=>{l.queryForm=e},"label-position":"right","label-width":"auto",class:"px-5 pt-5"},{default:d((()=>[n(W,{label:"地址",prop:"sqlIp"},{default:d((()=>[n(w,{modelValue:_.value.sqlIp,"onUpdate:modelValue":o[7]||(o[7]=e=>_.value.sqlIp=e),placeholder:"例如: 127.0.0.1",clearable:"","show-password":"",onKeyup:c(T,["enter","native"])},null,8,["modelValue","onKeyup"])])),_:1}),n(W,{label:"端口",prop:"ipPort"},{default:d((()=>[n(w,{modelValue:_.value.ipPort,"onUpdate:modelValue":o[8]||(o[8]=e=>_.value.ipPort=e),placeholder:"例如: 3306",clearable:"","show-password":"",onKeyup:c(T,["enter","native"])},null,8,["modelValue","onKeyup"])])),_:1}),n(W,{label:"用户名",prop:"sqlUsername"},{default:d((()=>[n(w,{modelValue:_.value.sqlUsername,"onUpdate:modelValue":o[9]||(o[9]=e=>_.value.sqlUsername=e),placeholder:"例如: DullFan",clearable:"","show-password":"",onKeyup:c(T,["enter","native"])},null,8,["modelValue","onKeyup"])])),_:1}),n(W,{label:"密码",prop:"sqlPassword"},{default:d((()=>[n(w,{modelValue:_.value.sqlPassword,"onUpdate:modelValue":o[10]||(o[10]=e=>_.value.sqlPassword=e),placeholder:"例如: 123456",clearable:"","show-password":"",onKeyup:c(T,["enter","native"])},null,8,["modelValue","onKeyup"])])),_:1}),n(W,{label:"数据库",prop:"sqlPassword"},{default:d((()=>[n(w,{modelValue:_.value.sqlName,"onUpdate:modelValue":o[11]||(o[11]=e=>_.value.sqlName=e),placeholder:"例如: code_test",clearable:"","show-password":"",onKeyup:c(T,["enter","native"])},null,8,["modelValue","onKeyup"])])),_:1})])),_:1},8,["model"])])),_:1},8,["modelValue"]),n(pe,{modelValue:l.value,"onUpdate:modelValue":o[16]||(o[16]=e=>l.value=e),title:"SQL生成",width:"500"},{footer:d((()=>[p("div",I,[n(ue,{onClick:o[15]||(o[15]=e=>l.value=!1)},{default:d((()=>[E])),_:1}),n(ue,{type:"primary",onClick:Y},{default:d((()=>[R])),_:1})])])),default:d((()=>[n(W,{prop:"sqlPassword"},{default:d((()=>[n(w,{rows:10,type:"textarea",modelValue:_.value.sqlStatement,"onUpdate:modelValue":o[14]||(o[14]=e=>_.value.sqlStatement=e),placeholder:"只限Create",clearable:""},null,8,["modelValue"])])),_:1})])),_:1},8,["modelValue"]),n(pe,{modelValue:g.value,"onUpdate:modelValue":o[19]||(o[19]=e=>g.value=e),title:"导出代码",width:"500"},{footer:d((()=>[p("div",j,[n(ue,{onClick:o[18]||(o[18]=e=>g.value=!1)},{default:d((()=>[D])),_:1}),n(ue,{type:"primary",onClick:ee},{default:d((()=>[$])),_:1})])])),default:d((()=>[n(W,{prop:"sqlPassword"},{default:d((()=>[n(w,{rows:10,type:"textarea",modelValue:_.value.exportLocal,"onUpdate:modelValue":o[17]||(o[17]=e=>_.value.exportLocal=e),placeholder:"路径(需要先完成配置数据库并且还要在本地运行)",clearable:""},null,8,["modelValue"])])),_:1})])),_:1},8,["modelValue"])],64)}}});G.__scopeId="data-v-7fc06162";const O={class:"app-wrapper"};const Q=_({setup:e=>(e,l)=>(i(),r("div",O,[n(G)]))});Q.config.globalProperties.$api="http://localhost:9968/api",Q.use(k).mount("#app");
