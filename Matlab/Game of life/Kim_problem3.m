function varargout = Kim_problem3(varargin)
% KIM_PROBLEM3 MATLAB code for Kim_problem3.fig
%      KIM_PROBLEM3, by itself, creates a new KIM_PROBLEM3 or raises the existing
%      singleton*.
%
%      H = KIM_PROBLEM3 returns the handle to a new KIM_PROBLEM3 or the handle to
%      the existing singleton*.
%
%      KIM_PROBLEM3('CALLBACK',hObject,eventData,handles,...) calls the local
%      function named CALLBACK in KIM_PROBLEM3.M with the given input arguments.
%
%      KIM_PROBLEM3('Property','Value',...) creates a new KIM_PROBLEM3 or raises the
%      existing singleton*.  Starting from the left, property value pairs are
%      applied to the GUI before Kim_problem3_OpeningFcn gets called.  An
%      unrecognized property name or invalid value makes property application
%      stop.  All inputs are passed to Kim_problem3_OpeningFcn via varargin.
%
%      *See GUI Options on GUIDE's Tools menu.  Choose "GUI allows only one
%      instance to run (singleton)".
%
% See also: GUIDE, GUIDATA, GUIHANDLES

% Edit the above text to modify the response to help Kim_problem3

% Last Modified by GUIDE v2.5 11-Jan-2024 18:35:50

% Begin initialization code - DO NOT EDIT
gui_Singleton = 1;
gui_State = struct('gui_Name',       mfilename, ...
                   'gui_Singleton',  gui_Singleton, ...
                   'gui_OpeningFcn', @Kim_problem3_OpeningFcn, ...
                   'gui_OutputFcn',  @Kim_problem3_OutputFcn, ...
                   'gui_LayoutFcn',  [] , ...
                   'gui_Callback',   []);
if nargin && ischar(varargin{1})
    gui_State.gui_Callback = str2func(varargin{1});
end

if nargout
    [varargout{1:nargout}] = gui_mainfcn(gui_State, varargin{:});
else
    gui_mainfcn(gui_State, varargin{:});
end
% End initialization code - DO NOT EDIT


% --- Executes just before Kim_problem3 is made visible.
function Kim_problem3_OpeningFcn(hObject, eventdata, handles, varargin)
% This function has no output args, see OutputFcn.
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
% varargin   command line arguments to Kim_problem3 (see VARARGIN)

% Choose default command line output for Kim_problem3
handles.output = hObject;

% Update handles structure
guidata(hObject, handles);

% UIWAIT makes Kim_problem3 wait for user response (see UIRESUME)
% uiwait(handles.figure1);


% --- Outputs from this function are returned to the command line.
function varargout = Kim_problem3_OutputFcn(hObject, eventdata, handles) 
% varargout  cell array for returning output args (see VARARGOUT);
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Get default command line output from handles structure
varargout{1} = handles.output;

% --- Executes on button press in Import_RLE.
function Import_RLE_Callback(hObject, eventdata, handles)
% hObject    handle to Import_RLE (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

%This gets the txt file the the user puts in
input_RLE_file = get(handles.Edit_RLE, 'String');
new_data = RLE_Decoder(input_RLE_file);
setappdata(handles.Edit_RLE, 'pre_matrix', new_data);

% --- Executes on button press in Random_Matrix.
function Random_Matrix_Callback(hObject, eventdata, handles)
% hObject    handle to Random_Matrix (see GCBO)
% eventdata  reserved - to be defeined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

%This creates a random matrix that is 10x in size that is comprised of 1
%and 0's and sets it to the value of pre_matrixe
outline = round(rand(50), 0);
new_matrix = padarray(outline,[100,100]);

setappdata(handles.Edit_RLE, 'pre_matrix', new_matrix);


% --- Executes on button press in Movie.
function Movie_Callback(hObject, eventdata, handles)
% hObject    handle to Movie (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
%This sets the FPS
FPS = 1;
if (getappdata(handles.FPS, "FPS_Pressed") == 1)
    FPS = getappdata(handles.FPS, "FPS");
end
G = getappdata(handles.Load, "Frames");
video = VideoWriter('Kim_movie_week3', 'MPEG-4');
%This gets each and every frame and then records it
video.FrameRate = FPS;
open(video)
for i = 1: length(G)
    imwrite(G{i}, 'Kim_Frame.png');
    [X,map] = imread("Kim_Frame.png");
    writeVideo(video, X);
end

close(video)




% --- Executes on selection change in Generations.
function Generations_Callback(hObject, eventdata, handles)
% hObject    handle to Generations (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: contents = cellstr(get(hObject,'String')) returns Generations contents as cell array
%        contents{get(hObject,'Value')} returns selected item from Generations

%Gets the contents of the value of generations
contents = cellstr(get(hObject,'String'));
value = contents{get(hObject, 'Value')};

%Changes the value into a numerical form
number = regexp(value, "\d*", "match");
new_generations = str2double(number{1,1});

%Sets it to a callable function
setappdata(handles.Generations, "Generations", new_generations);

%Sets up to make sure that it is already called to help with default
setappdata(handles.Generations, "Generations_Pressed", 1);
%Sets the title


% --- Executes during object creation, after setting all properties.
function Generations_CreateFcn(hObject, eventdata, handles)
% hObject    handle to Generations (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: popupmenu controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end


% --- Executes on button press in Load.
function Load_Callback(hObject, eventdata, handles)
% hObject    handle to Load (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

%Sets the variables 
run_matrix = getappdata(handles.Edit_RLE, 'pre_matrix');
setappdata(handles.Load, "run_matrix", run_matrix);

setappdata(handles.Play, "clear", 1);

%Draws the first frame
im = imagesc(run_matrix);

colormap("gray");
drawnow

%Records the first frame
F(1) = im;

G{1} = im.CData;

title("1 Generation");
setappdata(handles.Load, "Kim_frame", F(1).CData)
setappdata(handles.Load, "Frames", G)
setappdata(handles.Load, "Index", 2)




% --- Executes on button press in Step.
function Step_Callback(hObject, eventdata, handles)
% hObject    handle to Step (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

%Sets this up so it won't run if stop is toggled on
runner = 0;
if(getappdata(handles.Stop, "stopping") == 0)
    runner = 1;
%This is for the start of the matrix and makes it so that the running can
%still happen before stop button being pressed
elseif(isempty(getappdata(handles.Stop, "stopping")) == 1)
    runner = 1;
end
%Doesn't step while running
if(getappdata(handles.Play, "running") == 1)
    runner = 0;
end

G = getappdata(handles.Load, "Frames");
index = getappdata(handles.Load, "Index");
if(runner == 1)
    
    Original = getappdata(handles.Load, "run_matrix");
    
    new_gen = GOL(Original);
    %Draws the frame
    im = imagesc(new_gen);
    colormap("gray");
    drawnow
    
    %Records the frame
    F(index) = im;
    G{index} = im.CData;
    setappdata(handles.Load, "Kim_frame", F(index).CData)
    
    title(num2str(index) + " Generations");
    index = index + 1;
    setappdata(handles.Load, "run_matrix", new_gen)

end
setappdata(handles.Load, "Frames", G)
setappdata(handles.Load, "Index", index)

% --- Executes on button press in Play.
function Play_Callback(hObject, eventdata, handles)
% hObject    handle to Play (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

%This won't run if the stop button is activiated


if(getappdata(handles.Stop, "stopping") == 0)
    setappdata(handles.Play, "running", 1);
%This is for the start of the matrix and makes it so that the running can
%still happen before stop button being pressed
elseif(isempty(getappdata(handles.Stop, "stopping")) == 1)
    setappdata(handles.Play, "running", 1);
end

%Sets the generations that it will take
generations = 1;
if(getappdata(handles.Generations, "Generations_Pressed") == 1)
    generations = getappdata(handles.Generations, "Generations");
end

%Sets default FPS
FPS = 1;

%Gets the data for the matrix
Original = getappdata(handles.Load, "run_matrix");
new_gen = Original;

if(isempty(getappdata(handles.Load, "run_matrix")) == 1)
    setappdata(handles.Play, "running", 0)
end
G = getappdata(handles.Load, "Frames");
index = getappdata(handles.Load, "Index");
if(getappdata(handles.Play, "running") == 1)

    for i = 1:generations

        if (getappdata(handles.FPS, "FPS_Pressed") == 1)
            FPS = getappdata(handles.FPS, "FPS");
        end

        %Sets the FPS and accounts for how long it takess the program to
        %run
        pause(1/FPS-0.039);
        
        Original = getappdata(handles.Load, "run_matrix");
        new_gen = GOL(Original);
        %Draws the frame

        im=imagesc(new_gen);
        colormap("gray");
        title(num2str(index) + " Generations");
        drawnow
        
        index = index + 1;
        %Records the frame
        F(index) = im;
        G{index} = im.CData;
        setappdata(handles.Load, "Kim_frame", F(index).CData)
        

        %This stops the code depending on certain buttons
        if(getappdata(handles.Play, "clear") == 0)
            setappdata(handles.Load, "run_matrix", []);
            break;
        end
        while(getappdata(handles.Stop, "stopping") == 1)
            pause(5/1000)
            %Error Handling
            if(getappdata(handles.Play, "clear") == 0)
                setappdata(handles.Load, "run_matrix", []);
                break;
            end
        end
        if(getappdata(handles.Play, "running") == 0)
            break;
        end
        setappdata(handles.Load, "run_matrix", new_gen)

    end
   
    
end
%Error Handling
if(getappdata(handles.Play, "clear") == 0)
    setappdata(handles.Load, "run_matrix", []);
end

%This is no longer running
setappdata(handles.Play, "running", 0)
setappdata(handles.Load, "Frames", G)
setappdata(handles.Load, "Index", index)


% --- Executes on button press in Clear.
function Clear_Callback(hObject, eventdata, handles)
% hObject    handle to Clear (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

%Clears everything and stops the matrix
setappdata(handles.Play, "running", 0)
setappdata(handles.Play, "clear", 0);
setappdata(handles.Load, "run_matrix", []);


%Draws the frame
im = imagesc(getappdata(handles.Load, "run_matrix"));
colormap("gray");
drawnow

%Records the frame
F = im;
title("No Generations");
setappdata(handles.Load, "Kim_Frame", F.CData)
setappdata(handles.Load, "Frames", F.CData)

% --- Executes on button press in Stop.
function Stop_Callback(hObject, eventdata, handles)
% hObject    handle to Stop (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hint: get(hObject,'Value') returns toggle state of Stop
%This toggles the stop calue and when on turns off the running and makes it
%so that it can't run until toggled off
if(get(hObject, 'Value') == 1)
    setappdata(handles.Stop, "stopping", 1);
else
    setappdata(handles.Stop, "stopping", 0);
end


% --- Executes on selection change in FPS.
function FPS_Callback(hObject, eventdata, handles)
% hObject    handle to FPS (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: contents = cellstr(get(hObject,'String')) returns FPS contents as cell array
%        contents{get(hObject,'Value')} returns selected item from FPS

%Gets the contents of the value of FPS
contents = cellstr(get(hObject,'String'));
value = contents{get(hObject, 'Value')};

%Changes the value into a numerical form
number = regexp(value, "\d*", "match");
new_FPS = str2double(number{1,1});

%Sets it to a callable function
setappdata(handles.FPS, "FPS", new_FPS);

%Sets up to make sure that it is already called to help with default
setappdata(handles.FPS, "FPS_Pressed", 1);

% --- Executes during object creation, after setting all properties.
function FPS_CreateFcn(hObject, eventdata, handles)
% hObject    handle to FPS (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: popupmenu controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end


function Edit_RLE_Callback(hObject, eventdata, handles)
% hObject    handle to Edit_RLE (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of Edit_RLE as text
%        str2double(get(hObject,'String')) returns contents of Edit_RLE as a double


% --- Executes during object creation, after setting all properties.
function Edit_RLE_CreateFcn(hObject, eventdata, handles)
% hObject    handle to Edit_RLE (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end


% --- Executes on button press in Frame.
function Frame_Callback(hObject, eventdata, handles)
% hObject    handle to Frame (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
Frame = getappdata(handles.Load, "Kim_frame");
imwrite(Frame, 'Kim_Frame.png');
set(handles.Frame_Output, 'String', 'Kim_Frame.png')

function Frame_Output_Callback(hObject, eventdata, handles)
% hObject    handle to Frame_Output (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of Frame_Output as text
%        str2double(get(hObject,'String')) returns contents of Frame_Output as a double


% --- Executes during object creation, after setting all properties.
function Frame_Output_CreateFcn(hObject, eventdata, handles)
% hObject    handle to Frame_Output (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end


% --- Executes on selection change in popupmenu4.
function popupmenu4_Callback(hObject, eventdata, handles)
% hObject    handle to popupmenu4 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: contents = cellstr(get(hObject,'String')) returns popupmenu4 contents as cell array
%        contents{get(hObject,'Value')} returns selected item from popupmenu4


% --- Executes during object creation, after setting all properties.
function popupmenu4_CreateFcn(hObject, eventdata, handles)
% hObject    handle to popupmenu4 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: popupmenu controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end


% --- Executes on button press in Export_RLE.
function Export_RLE_Callback(hObject, eventdata, handles)
% hObject    handle to Export_RLE (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
my_matrix = getappdata(handles.Load, "run_matrix");
my_txt = RLE_Encoder(my_matrix);
writecell(my_txt, 'Kim_RLE.txt');

set(handles.RLE_Export_File, 'String', 'Kim_RLE.txt')


function RLE_Export_File_Callback(hObject, eventdata, handles)
% hObject    handle to RLE_Export_File (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of RLE_Export_File as text
%        str2double(get(hObject,'String')) returns contents of RLE_Export_File as a double


% --- Executes during object creation, after setting all properties.
function RLE_Export_File_CreateFcn(hObject, eventdata, handles)
% hObject    handle to RLE_Export_File (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end
