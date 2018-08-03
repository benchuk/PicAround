//
//  GalleryView.m
//  PicAround
//
//  Created by BenA on 2/10/13.
//  Copyright (c) 2013 BenA. All rights reserved.
//

#import "GalleryView.h"
#import "PACollectionViewCell.h"
#import "PAImageDetailController.h"
@implementation GalleryView

-(void) viewDidLoad
{
    self.searches = [@[] mutableCopy];
    self.searchResults = [@{} mutableCopy];
    self.paApi = [[PicAroundApi alloc]init];
    [self.paApi albumImagesById: @"1" completionBlock:^(NSString *searchTerm, NSArray *results, NSError *error) {
      
         if(results && [results count] > 0)
         {
             self.searchResults = results;
             dispatch_async(dispatch_get_main_queue(), ^{
                 [self.collectionView reloadData];
             });
         }
    }];
    [super viewDidLoad];
}
#pragma mark - UICollectionView Datasource
// 1
- (NSInteger)collectionView:(UICollectionView *)view numberOfItemsInSection:(NSInteger)section {
   
    return [self.searchResults count];
}
// 2
- (NSInteger)numberOfSectionsInCollectionView: (UICollectionView *)collectionView {
    return 1;
}
// 3
- (UICollectionViewCell *)collectionView:(UICollectionView *)cv cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    PACollectionViewCell *cell =  (PACollectionViewCell *)[cv dequeueReusableCellWithReuseIdentifier:@"Cell" forIndexPath:indexPath];
    cell.backgroundColor = [UIColor whiteColor];
    //cell.imageView.image = load image from internet;
    cell.imageView.image  = ((PAPhotoEntity *)[self.searchResults objectAtIndex:[indexPath row]]).Thumbnail;
    return cell;
}
// 4
/*- (UICollectionReusableView *)collectionView:
 (UICollectionView *)collectionView viewForSupplementaryElementOfKind:(NSString *)kind atIndexPath:(NSIndexPath *)indexPath
 {
 return [[UICollectionReusableView alloc] init];
 }*/


#pragma mark - UICollectionViewDelegate
- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
    // TODO: Select Item
}
- (void)collectionView:(UICollectionView *)collectionView didDeselectItemAtIndexPath:(NSIndexPath *)indexPath {
    // TODO: Deselect item
}


-(void) prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
   //PAPhotoDetailSegue
    if([segue.identifier isEqualToString: @"PAPhotoDetailSegue"])
    {
       PACollectionViewCell *cell = (PACollectionViewCell *)sender;
        NSIndexPath *indexPath = [self.collectionView indexPathForCell:cell];
        //Select the image from source
        PAImageDetailController* detailedView = (PAImageDetailController* )[segue destinationViewController];
        //detailedView.img = load image from internet;
        detailedView.img = ((PAPhotoEntity *)[self.searchResults objectAtIndex:[indexPath row]]).LargeImage;
    }
    
}

@end
