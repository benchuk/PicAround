//
//  PANewEventViewController.m
//  PicAround
//
//  Created by BenA on 2/23/13.
//  Copyright (c) 2013 BenA. All rights reserved.
//

#import "PANewEventViewController.h"
#import "PicAroundAppDelegate.h"
#import "PicAroundApi.h"
#import "PicAroundFirstViewController.h"
@interface PANewEventViewController () <FBPlacePickerDelegate>

@end

@implementation PANewEventViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.trackedViewName = @"Add New Event Screen";

    self.spinner.hidesWhenStopped = YES;
    [self.spinner stopAnimating];
   	// Do any additional setup after loading the view.
}
-(void) viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    
    PicAroundFirstViewController* parent = ((PicAroundFirstViewController*)self.presentingViewController.childViewControllers[0]);
    if (parent!= nil)
    {
        PicAroundAppDelegate *appDelegate = [[UIApplication sharedApplication]delegate];
        parent.DidCreatedNewEvent = appDelegate.NewEventCreated;
    }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewDidUnload {
    [self setEventName:nil];
    [self setEventDuration:nil];
    [self setEventStartDate:nil];
    [self setNewEventName:nil];
    [self setSpinner:nil];
    [super viewDidUnload];
}
- (IBAction)done:(id)sender {

    if ([_NewEventName.text length] == 0)
    {
        
        
        UIAlertView *alert = [[UIAlertView alloc]
                              initWithTitle: @"Event Name Empty"
                              message: @"Event name cannot be empty, please type an Event Name"
                              delegate: nil
                              cancelButtonTitle:@"OK"
                              otherButtonTitles:nil];
        [alert show];     
        return;
    }
    if ([eventLocationTextField.text length] == 0)
    {
        UIAlertView *alert = [[UIAlertView alloc]
                              initWithTitle: @"Location was not selected"
                              message: @"Location cannot be empty, please select a Location"
                              delegate: nil
                              cancelButtonTitle:@"OK"
                              otherButtonTitles:nil];
        [alert show];
        return;
    }
    
    [self.view endEditing:YES];
    PicAroundAppDelegate *appDelegate = [[UIApplication sharedApplication]delegate];
    appDelegate.NewEventCreated = YES;
    appDelegate.NewEventName = self.NewEventName.text;
    PicAroundApi* paApi = [[PicAroundApi alloc]init];
    //NSDateFormatter* f = [[NSDateFormatter alloc]init];
    //[f setDateFormat:@"yyyMMdd"];
    NSDate* NOW = [[NSDate alloc] init];
    NSTimeInterval secondsPerDay = 24*60*60;
    NSDate* tomorrow = [[[NSDate alloc]init] dateByAddingTimeInterval:secondsPerDay];
    NSTimeInterval ts = [NOW timeIntervalSince1970];
    NSTimeInterval te = [tomorrow timeIntervalSince1970];
    long ds = (long) ts;
    long de = (long) te;
    int dds = (int)(fmod(ts,1)*1000);
    int dde = (int)(fmod(te,1)*1000);
    NSString* timeNow = [NSString stringWithFormat:@"%ld%d",ds,dds];
    NSString* timeTomorrow = [NSString stringWithFormat:@"%ld%d",de,dde];
    self.spinner.hidesWhenStopped = YES;
    [self.spinner startAnimating];
    [paApi createNewEvent:appDelegate.user.id withEventName:self.NewEventName.text withEventStartTime:timeNow withEventEndTime:timeTomorrow withPlaceLat:appDelegate.Latitude withPlaceLong:appDelegate.Longitude withLocationId:appDelegate.NewEventLocationName completionBlock:^(NSString *res){
        
        dispatch_async(dispatch_get_main_queue(), ^{
            self.spinner.hidesWhenStopped = YES;
            [self.spinner stopAnimating];
            
            
            [self dismissModalViewControllerAnimated:YES];
            
        });
    }];
    
}


- (IBAction)Cancel:(id)sender {
    
    PicAroundAppDelegate *appDelegate = [[UIApplication sharedApplication]delegate];
    appDelegate.NewEventCreated = NO;
    appDelegate.NewEventName = nil;
    [self dismissModalViewControllerAnimated:YES];
}

- (void)setPlaceCacheDescriptorForCoordinates:(CLLocationCoordinate2D)coordinates {
    self.placeCacheDescriptor =
    [FBPlacePickerViewController cacheDescriptorWithLocationCoordinate:coordinates
                                                        radiusInMeters:1000
                                                            searchText:@""
                                                          resultsLimit:50
                                                      fieldsForRequest:nil];
}

- (IBAction)SelectLocation:(id)sender {
    FBPlacePickerViewController *placePicker = [[FBPlacePickerViewController alloc] init];
    
    placePicker.title = @"Where are you?";
    
    // SIMULATOR BUG:
    // See http://stackoverflow.com/questions/7003155/error-server-did-not-accept-client-registration-68
    // at times the simulator fails to fetch a location; when that happens rather than fetch a
    // a meal near 0,0 -- let's see if we can find something good in Paris
    PicAroundAppDelegate *appDelegate = [[UIApplication sharedApplication]delegate];
    if (self.placeCacheDescriptor == nil) {
        [self setPlaceCacheDescriptorForCoordinates:CLLocationCoordinate2DMake(appDelegate.Location.coordinate.latitude, appDelegate.Location.coordinate.longitude)];
    }
    
    [placePicker configureUsingCachedDescriptor:self.placeCacheDescriptor];
    [placePicker loadData];
    [placePicker presentModallyFromViewController:self
                                         animated:YES
                                          handler:^(FBViewController *sender, BOOL donePressed) {
                                              if (donePressed) {
                                                  self.selectedPlace = placePicker.selection;
                                                  eventLocationTextField.text = self.selectedPlace.name;
                                                  appDelegate.NewEventLocationName = self.selectedPlace.name;
                                              }
                                          }];
}

-(void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
    UITouch* touch = [[event allTouches] anyObject];
   
    [self.view endEditing:YES];
   
    if([touch view] == eventLocationTextField)
    {
        [eventLocationTextField resignFirstResponder];
        [self SelectLocation:self];
        [self.view endEditing:YES];
    }
}

- (IBAction)click:(id)sender {
    [self SelectLocation:self];
}


@end
