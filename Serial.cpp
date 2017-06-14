/*

    C++ program to receive r,g,and b values as command line parameters and provide smooth transition
    between 2 colors.

*/

#include<windows.h>
#define INTERPOLATION_DELAY 500

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

    //create serial port
	hComPort = CreateFile(lPortName, GENERIC_READ | GENERIC_WRITE, 0, NULL, OPEN_EXISTING, 0, NULL);
	PortDCB.DCBlength = sizeof(DCB);
	GetCommState(hComPort, &PortDCB);
	PortDCB.BaudRate = 57600;
	PortDCB.ByteSize = 8;
	PortDCB.Parity = NOPARITY;
	PortDCB.StopBits = ONESTOPBIT;

	SetCommState(hComPort, &PortDCB);
	unsigned char outputArray[11]; //final output array in bytes

	if (argc != 7) return 1; //check argument count

	int r = to_int(argv[1]); //red value
	int g = to_int(argv[2]); //green value
	int b = to_int(argv[3]); //blue value
	int or = to_int(argv[4]); //old red value
	int og = to_int(argv[5]); //old green value
	int ob = to_int(argv[6]); //old blue value

	for (int i = 0; i < INTERPOLATION_DELAY;i++){ //loop runs once per ms (ideally)

        //linear interpolation between colors
		int nr = or + (r-or)*i/500.0;
		int ng = og + (g-og)*i/500.0;
		int nb = ob + (b-ob)*i/500.0;

        // the Arduino receives 11 bytes serially where the second byte is green, the 6th is red and
        // the 11th is blue. First byte is for synchronization.
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
        //send data to serial port
		WriteFile(hComPort, outputArray, 11, &dwritten, &osWrite);
		Sleep(1); //sleep for 1ms
	}


}