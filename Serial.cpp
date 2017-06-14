#include<windows.h>

using namespace std;

int to_int(char const *s)
{
	if (s == NULL || *s == '\0')
		return -1;

	bool negate = (s[0] == '-');
	if (*s == '+' || *s == '-')
		++s;

	if (*s == '\0')
		return -1;

	int result = 0;
	while (*s)
	{
		if (*s >= '0' && *s <= '9')
		{
			result = result * 10 - (*s - '0');  //assume negative number
		}
		else
			return -1;
		++s;
	}
	return negate ? result : -result; //-result is positive!
}

int main(int argc,char** argv) {
	DCB PortDCB;
	LPTSTR lPortName = TEXT("COM2");
	HANDLE hComPort;
	DWORD dwritten=0;
	OVERLAPPED osWrite = { 0 };

	hComPort = CreateFile(lPortName, GENERIC_READ | GENERIC_WRITE, 0, NULL, OPEN_EXISTING, 0, NULL);
	PortDCB.DCBlength = sizeof(DCB);
	GetCommState(hComPort, &PortDCB);
	PortDCB.BaudRate = 57600;
	PortDCB.ByteSize = 8;
	PortDCB.Parity = NOPARITY;
	PortDCB.StopBits = ONESTOPBIT;

	SetCommState(hComPort, &PortDCB);
	unsigned char outputArray[11];

	if (argc != 7) return 1;

	int r = to_int(argv[1]);
	int g = to_int(argv[2]);
	int b = to_int(argv[3]);
	int or = to_int(argv[4]);
	int og = to_int(argv[5]);
	int ob = to_int(argv[6]);

	for (int i = 0; i < 500;i++){

		int nr = or + (r-or)*i/500.0;
		int ng = og + (g-og)*i/500.0;
		int nb = ob + (b-ob)*i/500.0;

		outputArray[0] = 255;
		outputArray[1] = (unsigned char)ng;
		outputArray[2] = 255;
		outputArray[3] = 255;
		outputArray[4] = 255;
		outputArray[5] = (unsigned char)nr;
		outputArray[6] = 255;
		outputArray[7] = 255;
		outputArray[8] = 255;
		outputArray[9] = 255;
		outputArray[10] = (unsigned char)nb;

		WriteFile(hComPort, outputArray, 11, &dwritten, &osWrite);
		Sleep(1);
	}


}