import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpPage(
    navigationBack: () -> Unit,
) {
    Scaffold(
        containerColor = Color.DarkGray,
        topBar = {
            TopAppBar(
                title = { Text("도움말", color = Color.White, fontSize = 24.sp) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.DarkGray,
                ),
                actions = {
                    IconButton(onClick = navigationBack) {
                        Icon(
                            tint = Color.White,
                            imageVector = Icons.Default.Close,
                            contentDescription = "Back",
                            modifier = Modifier.size(34.dp)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        // ✅ 전체 배경을 DarkGray로
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.DarkGray)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp), // 내용 여백
                verticalArrangement = Arrangement.Top
            ) {
                Text("취지", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "저희 앱은 자체 인터넷으로 영상을 전송하는 몰래 카메라를 탐지하기 위한 앱이고, 아래와 같은 서비스를 제공합니다.\n" +
                            "1) IP 카메라의 유무\n" +
                            "2) IP 카메라의 정확한 위치 찾기",
                    fontSize = 16.sp,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text("사용법", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "1. 홈에서 수상한 기기를 검색하세요.\n" +
                            "2. 수상한 기기 찾기 버튼을 눌러 고정하고, 하단 왼쪽 메뉴를 통해 정확한 위치를 찾으세요.",
                    fontSize = 16.sp,
                    color = Color.White
                )


                Spacer(modifier = Modifier.height(24.dp))


                Text("동작 원리", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)


                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    buildAnnotatedString {
                        append(
                            "1. 수상한 기기 검색\n- 검색되는 wifi 중 카메라 영상을 보내는 기기를 머신러닝을 통해 찾습니다.\n\n" +
                                    "2. 기기의 정확한 위치 찾기\n- 가까운 거리에 수상한 기기가 존재할 경우, 삼변측량을 통해 x, y, z 축으로 정확한 위치 정보를 알려줍니다."
                        )
                    },
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }
    }
}
