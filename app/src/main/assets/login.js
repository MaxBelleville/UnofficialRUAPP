function start(){
if(document.getElementsByClassName('alert-info').length==1)return 2;
   if(window.sessionStorage.getItem('login')==null){
   window.sessionStorage.setItem('login',1);
   document.getElementById('username').value='GET_USERNAME';
   document.getElementById('password').value='GET_PASSWORD';
   var item=document.getElementsByClassName('btn')[0];
   item.removeAttribute('disabled');
   item.click();
   }
   else {
   window.sessionStorage.clear();
   return (document.getElementsByClassName('alert-danger').length==1)?0 : 1;
   }
}
start();
