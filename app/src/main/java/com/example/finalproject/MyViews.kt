package com.example.finalproject



import android.util.Log
import android.widget.ImageView
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.decode.ImageSource


const val HOME_ROUTE ="home"

const val NOTE_ROUTE ="note"
const val SEARCH ="search"

@Composable
fun MainView() {
    val userVM = viewModel<UserViewModel>()

    if(userVM.username.value.isEmpty())
    {
       LoginView(userVM)

    }
    else{
       MainScaffoldView(userVM)
    }
}

@Composable
fun MainScaffoldView(userVM: UserViewModel) {

    val navController= rememberNavController()

    Scaffold(
        topBar = {TopBarView()},
        bottomBar = { BottomBarView(navController)},
        content = { MainContentView(navController,userVM )},
    )
}

@Composable
fun TopBarView() {
    var userVM = viewModel<UserViewModel>()
    Row(modifier = Modifier
        .fillMaxWidth()
        .background(Color(0xFFF7CA43))
        .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically){

            Text(text = userVM.username.value)
        OutlinedButton(onClick = {userVM.logoutUser()}) {
            Text(text = "Log out")
        }
    }
}

@Composable
fun BottomBarView(navController: NavController) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .background(Color(0xFFF7CA43)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly)
    {
        Icon(painter = painterResource(id = R.drawable.ic_baseline_home_24) , contentDescription = "Home",
        modifier = Modifier.clickable { navController.navigate(HOME_ROUTE) })

        Icon(painter = painterResource(id = R.drawable.ic_baseline_search_24) , contentDescription = "Search",
            modifier = Modifier.clickable { navController.navigate(SEARCH) })

        Icon(painter = painterResource(id = R.drawable.ic_baseline_notes_24) , contentDescription = "Note",
            modifier = Modifier.clickable { navController.navigate(NOTE_ROUTE) })
    }

}


@Composable
fun MainContentView(navController: NavHostController,userVM: UserViewModel) {
    NavHost(navController = navController, startDestination = HOME_ROUTE ){
        composable(route = HOME_ROUTE){ HomeView()}
        composable(route = SEARCH){ SearchView()}
        composable(route = NOTE_ROUTE){ ConversationView(userVM)}

    }
}

@Composable
fun SearchView() {
    var search by remember {
        mutableStateOf("")
    }
    val vm= viewModel<CovidModel>()

    Column(modifier = Modifier.fillMaxWidth().height(200.dp)) {
        Row(){
            OutlinedTextField(
                value = search,
                onValueChange = {search = it},
                label ={Text(text="Search")})
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                onClick = {
                    vm.getCountry(search)
                }) {
                Text(text = "Send")
            }
        }
        Card(modifier = Modifier
            .padding(0.dp, 0.dp, 0.dp, 10.dp)
            .height(100.dp)
            .fillMaxWidth(),
        ) {
            Row(){
                AsyncImage(model =vm.flag.value.replace("\"",""),
                    contentDescription ="hahaha",
                    modifier = Modifier.size(100.dp) )
                Text(text = vm.country.value, modifier = Modifier.padding(10.dp,10.dp,0.dp,0.dp))
                Column(modifier = Modifier
                    .padding(20.dp, 0.dp, 0.dp, 10.dp)){
                    Text(text = "Total cases:" +vm.cases.value, modifier = Modifier.padding(0.dp,0.dp,0.dp,10.dp))
                    Text(text = "Total deaths:" +vm.deaths.value, modifier = Modifier.padding(0.dp,0.dp,0.dp,10.dp))
                    Text(text = "Total recovered:" +vm.recovered.value, modifier = Modifier.padding(0.dp,0.dp,0.dp,10.dp))
                }
            }



        }
    }







}

@Composable
fun HomeView() {
    val scrollState = rememberScrollState()
    val vm= viewModel<CovidModel>()
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFF46A232))
        .verticalScroll(state = scrollState)) {
        vm.covidInformation.value.forEach{
                //Text(text = it.'country'.toString())
            val FLAG = it["countryInfo"].asJsonObject["flag"].toString()

            Card(modifier = Modifier
                .padding(0.dp, 0.dp, 0.dp, 10.dp)
                .height(100.dp)
                .fillMaxWidth(),
              ) {
                Row(){
                    AsyncImage(model =FLAG.replace("\"",""),
                        contentDescription ="hahaha",
                        modifier = Modifier.size(100.dp) )
                    Text(text = it["country"].toString().replace("\"",""), modifier = Modifier.padding(10.dp,10.dp,0.dp,0.dp))
                    Column(modifier = Modifier
                        .padding(20.dp, 0.dp, 0.dp, 10.dp)){
                        Text(text = "Total cases:" + it["cases"].toString(), modifier = Modifier.padding(0.dp,0.dp,0.dp,10.dp))
                        Text(text = "Total deaths:" +it["deaths"].toString(), modifier = Modifier.padding(0.dp,0.dp,0.dp,10.dp))
                        Text(text = "Total recovered:" +it["recovered"].toString(), modifier = Modifier.padding(0.dp,0.dp,0.dp,10.dp))
                    }
                }



            }

        }



    }
}



@Composable
fun ConversationViewWindow() {

    val scrollState = rememberScrollState()

    val msgVM: MessageViewModel = viewModel()
    
    Column(modifier = Modifier
        .fillMaxSize()
        .background(
            Color(0xFF46A232)
        )
        .verticalScroll(state = scrollState),

        verticalArrangement = Arrangement.Top) {
       
        msgVM.msgs.value.forEach{
            Text(text = it)
            Spacer(modifier = Modifier.padding(0.dp,0.dp,0.dp,10.dp))
        }


    }

}

@Composable
fun ConversationView(userVM:UserViewModel) {


    ConversationViewWindow()
    MessageAdding(userVM)
}


@Composable
fun MessageAdding(userVM:UserViewModel) {
    var msgs by remember { mutableStateOf("")}
    val msgVM: MessageViewModel = viewModel()



    Column(modifier = Modifier
        .fillMaxSize()
        .padding(0.dp, 0.dp, 0.dp, 50.dp),
        verticalArrangement = Arrangement.Bottom

        ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,

            ) {

            OutlinedTextField(value = msgs, onValueChange ={msgs = it} )
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = { msgVM.addMessange(userVM.username.value,msgs)
                msgs=""
                }) {
                Text(text = "Send")
            }
        }
    }

}

@Composable
fun LoginView(userVM: UserViewModel) {

       var email by remember {
           mutableStateOf("")
       }
        var pw by remember {
        mutableStateOf("")
         }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment =  Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = {email = it},
            label ={Text(text="Email")})
        OutlinedTextField(
            value = pw,
            onValueChange = {pw = it},
            label ={Text(text="Password")},
            visualTransformation = PasswordVisualTransformation())
        Spacer(modifier = Modifier.padding(0.dp,0.dp,0.dp,5.dp))
        OutlinedButton(onClick = {userVM.loginUser(email,pw)}) {
            Text(text = "Login")
            
        }

    }
}
