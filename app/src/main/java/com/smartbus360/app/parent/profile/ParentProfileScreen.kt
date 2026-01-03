//package com.smartbus360.app.parent.profile
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.material3.*
//import androidx.compose.material3.CardDefaults.cardElevation
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import coil.compose.AsyncImage
//import com.smartbus360.app.R
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.shape.RoundedCornerShape
//import com.smartbus360.app.parent.models.ParentProfileResponse
//import com.smartbus360.app.parent.models.ChildInfo
//
//@Composable
//fun ParentProfileScreen(
//    onLogout: () -> Unit,
//    onSelectChild: (Int) -> Unit
//) {
//    val viewModel: ParentProfileViewModel = viewModel()
//    val data = viewModel.profile.collectAsState()
//
//    LaunchedEffect(Unit) {
//        viewModel.loadProfile()
//    }
//
//    if (data.value == null) {
//        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//            CircularProgressIndicator()
//        }
//        return
//    }
//
//    val profile = data.value!!
//
//    LazyColumn(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color(0xFFF8F9FA))
//            .padding(16.dp),
//        verticalArrangement = Arrangement.spacedBy(16.dp)
//    ) {
//
//        /** PARENT INFO CARD **/
//        item {
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                shape = RoundedCornerShape(20.dp),
//                elevation = cardElevation(6.dp)
//            ) {
//                Column(
//                    modifier = Modifier.padding(20.dp),
//                    verticalArrangement = Arrangement.spacedBy(10.dp)
//                ) {
//                    Text("Parent Profile", style = MaterialTheme.typography.headlineSmall)
//
//                    Text(profile.parent.fullName, fontWeight = FontWeight.Bold)
//                    Text(profile.parent.email)
//                    Text(profile.parent.phone)
//
//                    profile.parent.address?.let {
//                        Text("Address: $it")
//                    }
//                }
//            }
//        }
//
//        /** CHILDREN LIST **/
//        item {
//            Text(
//                "Children",
//                style = MaterialTheme.typography.headlineSmall,
//                fontWeight = FontWeight.Bold
//            )
//        }
//
//        items(profile.children.size) { index ->
//            val child = profile.children[index]
//
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .clickable { onSelectChild(child.id) },
//                shape = RoundedCornerShape(18.dp),
//                elevation = cardElevation(5.dp)
//            ) {
//                Row(
//                    modifier = Modifier.padding(16.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//
//                    AsyncImage(
//                        model = child.profilePic ?: R.drawable.images,
//                        contentDescription = "Profile",
//                        modifier = Modifier
//                            .size(60.dp)
//                            .padding(end = 12.dp),
//                        contentScale = ContentScale.Crop
//                    )
//
//                    Column {
//                        Text(child.name, fontWeight = FontWeight.Bold)
//                        Text("Class ${child.className} - ${child.section}")
//                        Text("Roll No: ${child.rollNumber}")
//                    }
//                }
//            }
//        }
//
//        /** LOGOUT BUTTON */
//        item {
//            Button(
//                onClick = onLogout,
//                modifier = Modifier.fillMaxWidth(),
//                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
//            ) {
//                Text("Logout", color = Color.White)
//            }
//        }
//    }
//}
