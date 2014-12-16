#include "stdafx.h"
#include <stdlib.h>
#include <stdio.h>
#include <math.h>
#include "interpolation.h"

using namespace alglib;


int main(int argc, char **argv)
{
    //
    // In this example we demonstrate linear fitting by f(x|a,b) = a*x+b
    // with simple constraint f(0)=0.
    //
    // We have:
    // * y - vector of experimental data
    // * fmatrix -  matrix of basis functions sampled at [0,1] with step 0.2:
    //                  [ 1.0   0.0 ]
    //                  [ 1.0   0.2 ]
    //                  [ 1.0   0.4 ]
    //                  [ 1.0   0.6 ]
    //                  [ 1.0   0.8 ]
    //                  [ 1.0   1.0 ]
    //              first column contains value of first basis function (constant term)
    //              second column contains second basis function (linear term)
    // * cmatrix -  matrix of linear constraints:
    //                  [ 1.0  0.0  0.0 ]
    //              first two columns contain coefficients before basis functions,
    //              last column contains desired value of their sum.
    //              So [1,0,0] means "1*constant_term + 0*linear_term = 0" 
    //
    real_1d_array y = "[0.072436,0.246944,0.491263,0.522300,0.714064,0.921929]";
    real_2d_array fmatrix = "[[1,0.0],[1,0.2],[1,0.4],[1,0.6],[1,0.8],[1,1.0]]";
    real_2d_array cmatrix = "[[1,0,0]]";
    ae_int_t info;
    real_1d_array c;
    lsfitreport rep;

    //
    // Constrained fitting without weights
    //
    lsfitlinearc(y, fmatrix, cmatrix, info, c, rep);
    printf("%d\n", int(info)); // EXPECTED: 1
    printf("%s\n", c.tostring(3).c_str()); // EXPECTED: [0,0.932933]

    //
    // Constrained fitting with individual weights
    //
    real_1d_array w = "[1, 1.414213, 1, 1, 1, 1]";
    lsfitlinearwc(y, w, fmatrix, cmatrix, info, c, rep);
    printf("%d\n", int(info)); // EXPECTED: 1
    printf("%s\n", c.tostring(3).c_str()); // EXPECTED: [0,0.938322]
    return 0;
}
