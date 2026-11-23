#include <stdio.h>
#include "operacije.h"

int main()
{
    printf("Kalkulator\n");
    double a,b;
    printf("Unesite a:");scanf("%lf",&a);
    printf("Unesite b:");scanf("%lf",&b);
    printf("Izaberite opciju: 1.Saberi  2.Oduzimanje 3.Mnozenje 4.Djeljenje\n");
    int opcija;
    scanf("%d",&opcija);
    if(opcija==1)
        printf("%lf",saberi(a,b));
    else if(opcija==2)
        printf("%lf",oduzmi(a,b));
    else if(opcija==3)
        printf("%lf",pomnozi(a,b));
    else if(opcija==2)
    {
        if(b==0)
            return ptintf("Dijeljenje sa 0 nije dozvoljeno.\n");
        printf("%lf",podijeli(a,b));
    }

    return 0;
}
