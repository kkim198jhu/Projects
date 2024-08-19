function next_gen_mat = GOL(in_mat)
A = in_mat;
B = A;
[x_size, y_size] = size(A);
arow = x_size;
acol = y_size;
for r = 1:x_size
    for c = 1:y_size
        counter = 0;
        %Counters that count the amount of live cells near original cells
        if r ~= 1 && A(r-1,c) == 1
            counter = counter + 1;
        end
        if r ~= arow && A(r+1,c) == 1
            counter = counter + 1;
        end
        if c ~= 1 && A(r,c - 1) == 1
            counter = counter + 1;
        end
        if c ~= acol && A(r,c + 1) == 1
            counter = counter + 1;
        end
        if r ~= 1 && c ~= 1 && A(r - 1,c - 1) == 1
            counter = counter + 1;
        end
        if r ~= 1 && c ~= acol && A(r - 1 ,c + 1) == 1
            counter = counter + 1;
        end
        if r ~= arow && c ~= 1 && A(r + 1,c - 1) == 1
            counter = counter + 1;
        end
        if r ~= arow && c ~= acol && A(r + 1, c + 1) == 1
            counter = counter + 1;
        end
            
        %Determines the status of cells
        if(A(r,c) == 1)
            if(counter < 2 || counter > 3)
                B(r,c) = 0;
            end
        end
        if(A(r,c) == 0)
            if(counter == 3)
                B(r,c) = 1;
            end
        end

    end
end   
next_gen_mat = B;
end