function schedule(){
if(window.sessionStorage.getItem('dateSet')==null){
var select=document.getElementsByName("startOfWeek")[1];
var date=new Date();
var year= date.getFullYear();
var month = date.getMonth();
if(month>=0&&month<=3) year--;
var oldDate = new Date('9/2/'+year);
var diffTime = date.getTime() - oldDate.getTime();
var diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
if(diffDays<0) select.selectedIndex=0;
else if(Math.floor(diffDays/7)>=14) select.selectedIndex= Math.floor(diffDays/7)-14;
else select.selectedIndex= Math.floor(diffDays/7);
window.sessionStorage.setItem("dateSet",1);
select.onchange();
return true;
}
else {
 var finalText="";
 var items=document.getElementsByClassName("schedListViewInformation");
 for (var i=0;i<items.length;i++) {
 var splitItems = items[i].innerText.trim().split("\n");
 for (var n=0;n<splitItems.length;n++){
 var item = (splitItems[n].trim().match(/[\w (]/g)+"").replace(/,/g,"");
 if(item!=""&&item!="null")finalText+=item+"&"}
 finalText= finalText.substr(0,finalText.length-1)+";";}
 return finalText;}}
 schedule();